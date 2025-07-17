package com.xiaohai.xiaohai_home.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * @author XiaoYunTao
 * @since 2025/7/17
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Tool(description = "获取天气信息")
    public String BatchGenerateTitles(String city) {
        logger.info("Received weather request: {}", city);
        return "17日（今天）\n" + city + " " +
                "多云\n" +
                "\n" +
                "34/26℃\n" +
                "\n" +
                " \n" +
                "<3级\n" +
                "\n" +
                "18日（明天）\n" +
                "小雨\n" +
                "\n" +
                "33/25℃\n" +
                "\n" +
                " \n" +
                "<3级\n" +
                "\n" +
                "19日（后天）\n" +
                "小雨转多云\n" +
                "\n" +
                "33/26℃\n" +
                "\n" +
                " \n" +
                "<3级\n" +
                "\n" +
                "20日（周日）\n" +
                "多云\n" +
                "\n" +
                "33/26℃\n" +
                "\n" +
                " \n" +
                "<3级\n" +
                "\n" +
                "21日（周一）\n" +
                "小雨转多云\n" +
                "\n" +
                "35/26℃\n" +
                "\n" +
                " \n" +
                "<3级\n" +
                "\n" +
                "22日（周二）\n" +
                "小雨转多云\n" +
                "\n" +
                "35/26℃\n" +
                "\n" +
                " \n" +
                "<3级\n" +
                "\n" +
                "23日（周三）\n" +
                "小雨\n" +
                "\n" +
                "34/27℃\n" +
                "\n" +
                " \n" +
                "<3级";
    }

}
