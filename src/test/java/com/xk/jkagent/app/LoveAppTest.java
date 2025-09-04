package com.xk.jkagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是小康";
        String content = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(content);
        // 第二轮
        message = "我最近和我对象*吵架了";
        content = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(content);
        // 第三轮
        message = "我刚说我对象叫啥来着";
        content = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(content);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是小康,我想让我的对象*更爱我，请给我一些建议";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRAG() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是小康,我发现在恋爱时我很依赖我的对象*，请给我一些建议";
        String content = loveApp.doChatWithRAG(message, chatId);
        Assertions.assertNotNull(content) ;
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");

        // 测试文件操作：保存用户档案
        testMessage("保存我的恋爱档案为文件");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMCP() {
        String chatId = UUID.randomUUID().toString();
//        String message = "你好，我是小康,我对象住在***，请帮我在5公里内找一些约会地点";
//        String answer = loveApp.doChatWithMCP(message, chatId);
        String message = "帮我搜索一些和女朋友开心逛街的图片";
        String answer = loveApp.doChatWithMCP(message, chatId);
        Assertions.assertNotNull(answer);
    }

}