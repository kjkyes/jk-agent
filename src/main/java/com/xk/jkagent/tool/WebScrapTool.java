package com.xk.jkagent.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * 网页抓取工具类（抓取指定网页的内容）
 */
public class WebScrapTool {
    @Tool(description = "Scarp web page content")
    public String doScrap(@ToolParam(description = "The required url") String url) {
        try {
            Document result = Jsoup.connect(url).get();
            return result.html();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
