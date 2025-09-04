package com.xk.xkimagesearchmcp;

import com.xk.xkimagesearchmcp.tools.PexelsSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class XkImageSearchMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(XkImageSearchMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider picturesSearchTools(PexelsSearchTool pexelsSearchTool){
        MethodToolCallbackProvider provider = MethodToolCallbackProvider.builder()
                .toolObjects(pexelsSearchTool)
                .build();
        return provider;
    }

}
