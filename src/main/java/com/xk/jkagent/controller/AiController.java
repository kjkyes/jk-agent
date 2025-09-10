package com.xk.jkagent.controller;

import com.xk.jkagent.agent.XKManus;
import com.xk.jkagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用LoveApp的ai对话
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/loveApp/chat/sync")
    public String doChatwithLoveAppSYNC(String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * SSE流式调用LoveApp的ai对话
     *
     * 方式一：需要加 http 的响应头
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/loveApp/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatwithLoveAppSSE(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId);
    }

    /**
     * SSE流式调用LoveApp的ai对话
     *
     * 方式二：将每个响应碎片包装成 ServerSentEvent 对象，框架会自带响应头
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/loveApp/chat/server_send_event")
    public Flux<ServerSentEvent<String>> doChatwithLoveAppServerSentEvent(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * SSE流式调用LoveApp的ai对话
     *
     * 方式三：将响应封装并返回一个 SseEmitter 对象，方便处理每个输出碎片
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/loveApp/chat/SseEmitter")
    public SseEmitter doChatwithLoveAppSseEmitter(String message, String chatId) {
        // 创建一个超时时间较长的SseEmitter
        SseEmitter sseEmitter = new SseEmitter(180000L);
        // 获取 Flux 响应式数据流并且直接通过订阅推送给 SseEmitter
        loveApp.doChatByStream(message, chatId)
                .subscribe(
                        // 处理每条消息
                        chunk -> {
                            try {
                                sseEmitter.send(chunk);
                            } catch (Exception e) {
                                sseEmitter.completeWithError(e);
                            }
                        },
                        // 处理异常
                        sseEmitter::completeWithError,
                        // 处理完成
                        sseEmitter::complete
                );
        return sseEmitter;
    }

    /**
     * 流式调用 XKManus 超级智能体
     * @param userPrompt
     * @return
     */
    @GetMapping(value = "/manus/chat")
    public SseEmitter doChatWithManus(String userPrompt) {
        XKManus manus = new XKManus(allTools, dashscopeChatModel);
        return manus.runSSE(userPrompt);
    }
}