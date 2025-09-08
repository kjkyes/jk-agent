package com.xk.jkagent.tool;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中注册工具类（给AI一次性提供全部工具）
 */
@Configuration
public class ToolRegistration {

    @Value("${search-api.api_key}")
    private String apiKey;

    @Bean
    public ToolCallback[] allTools(){
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(apiKey);
        WebScrapTool webScrapTool = new WebScrapTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScrapTool,
                terminalOperationTool,
                resourceDownloadTool,
                pdfGenerationTool,
                terminateTool
        );
    }
}
