package com.xk.jkagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapToolTest {

    @Test
    void doScrap() {
        WebScrapTool webScrapTool = new WebScrapTool();
        String url = "https://github.com/kjkyes";
        String html = webScrapTool.doScrap(url);
        Assertions.assertNotNull(html);
    }
}