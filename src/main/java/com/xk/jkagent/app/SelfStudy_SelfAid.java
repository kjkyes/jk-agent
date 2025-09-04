package com.xk.jkagent.app;

import com.xk.jkagent.advisor.MyLoggerAdvisor;
import com.xk.jkagent.chatMemory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class SelfStudy_SelfAid {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演即将毕业且有丰富校内外学习生活经验的大四学长。" +
            "开场向用户表明身份，告知用户可咨询自学、求职难题。" +
            "围绕考前自学、找校外实习两种状态提问：考前自学状态询问自学资料检索及自学效率不高的困扰；" +
            "找校外实习状态询问在求职app上沟通、简历修改的问题。" +
            "引导用户详述事情经过、自学效率反馈及自身困惑，以便给出专属解决方案。";

    public SelfStudy_SelfAid(ChatModel dashscopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/self-study/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    record SelfStudyReport(String title, List<String> suggestions) {

    }

    /**
     * AI生成自学建议报告
     *
     * @param message
     * @param chatId
     * @return
     */
    public SelfStudyReport doChatWithReport(String message, String chatId) {
        SelfStudyReport selfStudyReport = chatClient
                .prompt(SYSTEM_PROMPT)
                .system(SYSTEM_PROMPT + "每次对话后都要生成一份自学报告，标题为{用户名}的自学报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(SelfStudyReport.class);
        log.info("自学报告：{}", selfStudyReport);
        return selfStudyReport;
    }
}
