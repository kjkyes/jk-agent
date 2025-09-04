package com.xk.jkagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class SelfStudyTest {
    @Resource
    private SelfStudy_SelfAid selfAid;

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是小康,马上要期末考试了,我想让我的自学效率更高一些，请给我一些建议";
        SelfStudy_SelfAid.SelfStudyReport selfStudyReport = selfAid.doChatWithReport(message, chatId);
        Assertions.assertNotNull(selfStudyReport);
    }
}
