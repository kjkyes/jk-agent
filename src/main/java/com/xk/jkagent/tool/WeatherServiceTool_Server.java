package com.xk.jkagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceTool_Server {
    @Tool(description = "get the weather of the provided city")
    public String getWeather(@ToolParam(description = "the city name") String city) {
        // 模拟调用天气服务
        return "The weather in " + city + " is sunny.";
    }
}
