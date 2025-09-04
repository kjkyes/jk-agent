package com.xk.jkagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class xkManusTest {
    @Resource
    private XKManus xkManus;

    @Test
    void run(){
        String userPrompt = """
                我的对象居住在***，请帮我在5公里处找到适合的约会地点，
                并结合相关的图片，比如店面照片、美食、娱乐设施等，制定一份详细的约会计划
                """;
        String result = xkManus.run(userPrompt);
        Assertions.assertNotNull(result);
    }
}