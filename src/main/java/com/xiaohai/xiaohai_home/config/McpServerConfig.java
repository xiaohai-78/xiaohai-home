package com.xiaohai.xiaohai_home.config;

import com.xiaohai.xiaohai_home.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author XiaoYunTao
 * @since 2025/7/17
 */
@Configuration
@EnableWebMvc
public class McpServerConfig implements WebMvcConfigurer {
    @Bean
    public ToolCallbackProvider openLibraryTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }
}
