package com.xiaohai.xiaohai_home.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.observation.ChatClientObservationContext;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.util.json.JsonParser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoyuntao
 * @since 2025/06/26
 */
@Configuration
public class ObservationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ObservationConfig.class);

    @Bean
    @ConditionalOnMissingBean(name = "observationRegistry")
    public ObservationRegistry observationRegistry(
            ObjectProvider<ObservationHandler<?>> observationHandlerObjectProvider) {
        ObservationRegistry observationRegistry = ObservationRegistry.create();
        ObservationRegistry.ObservationConfig observationConfig = observationRegistry.observationConfig();
        observationHandlerObjectProvider.orderedStream().forEach(observationConfig::observationHandler);
        return observationRegistry;
    }

    @Bean
    ObservationHandler<ChatClientObservationContext> chatClientObservationContextObservationHandler() {
        logger.info("ChatClientObservation start");
        return new ObservationHandler<>() {

            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof ChatClientObservationContext;
            }

            @Override
            public void onStart(ChatClientObservationContext context) {
            }

            @Override
            public void onStop(ChatClientObservationContext context) {
                logger.info("[ChatClientObservationContext] stop context: {}", JsonParser.toJson(context));
            }
        };
    }

    @Bean
    ObservationHandler<ChatModelObservationContext> chatModelObservationContextObservationHandler() {
        logger.info("ChatModelObservation start");
        return new ObservationHandler<>() {
            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof ChatModelObservationContext;
            }

            @Override
            public void onStop(ChatModelObservationContext context) {
                Object limitedDepthObj = toLimitedDepth(context, 5);
                logger.info("[ChatModelObservation] stop context:\n {}", 
                    JSON.toJSONString(limitedDepthObj, SerializerFeature.PrettyFormat));
            }
        };
    }

    /**
     * 将对象转换为限制深度的结构
     * @param obj 要转换的对象
     * @param maxDepth 最大深度
     * @return 限制深度后的对象
     */
    private static Object toLimitedDepth(Object obj, int maxDepth) {
        return toLimitedDepth(obj, maxDepth, 0);
    }

    /**
     * 递归处理对象，限制序列化深度
     * @param obj 要处理的对象
     * @param maxDepth 最大深度
     * @param currentDepth 当前深度
     * @return 处理后的对象
     */
    private static Object toLimitedDepth(Object obj, int maxDepth, int currentDepth) {
        if (obj == null || currentDepth >= maxDepth) {
            return obj instanceof Object[] ? "..." : obj;
        }

        // 处理Map类型
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            Map<Object, Object> result = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(entry.getKey(), toLimitedDepth(entry.getValue(), maxDepth, currentDepth + 1));
            }
            return result;
        }

        // 处理集合类型
        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            List<Object> result = new ArrayList<>();
            for (Object item : col) {
                result.add(toLimitedDepth(item, maxDepth, currentDepth + 1));
            }
            return result;
        }

        // 处理数组
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                result.add(toLimitedDepth(Array.get(obj, i), maxDepth, currentDepth + 1));
            }
            return result;
        }

        // 如果是Java基本类型或其包装类，直接返回
        if (obj instanceof Number || obj instanceof Boolean || obj instanceof Character || 
            obj instanceof String || obj.getClass().isPrimitive() ||
            (obj.getClass().getPackage() != null && obj.getClass().getPackage().getName().startsWith("java.lang"))) {
            return obj;
        }

        // 处理普通Java对象
        Map<String, Object> result = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                // 跳过静态字段
                if (!Modifier.isStatic(field.getModifiers())) {
                    result.put(field.getName(), toLimitedDepth(value, maxDepth, currentDepth + 1));
                }
            } catch (Exception ignored) {
                // 如果获取字段值失败，放入一个占位符
                result.put(field.getName(), "...");
            }
        }
        return result;
    }
}
