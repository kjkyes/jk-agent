package com.xk.jkagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.xk.jkagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理工具调用的 基础 agent类，具体实现了 think 和 act 方法，可以用作创建实例的父类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    // 可用的工具
    private final ToolCallback[] availableTools;

    // 保存工具调用信息的响应结果（要调用哪些工具）
    private ChatResponse toolCallResponse;

    // 工具调用管理器
    private final ToolCallingManager toolCallManager;

    // 禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        // 调用下父组件的构造函数
        super();
        this.availableTools = availableTools;
        this.toolCallManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                // 阿里云的对话选项，传入true表示由我们自主维护选项与消息上下文
                .withProxyToolCalls(true)
                .build();
    }

    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动
     */
    @Override
    public boolean think() {
        // 1.校验提示词，拼接用户提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }
        try {
            // 2.调用 AI 大模型，获取工具调用结果
            List<Message> messages = getMessageList();
            Prompt prompt = new Prompt(messages, chatOptions);
            ChatResponse chatResponse = getChatClient()
                    .prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call().chatResponse();
            // 记录响应，稍后用于act()
            toolCallResponse = chatResponse;
            // 3.解析工具调用结果，获取要调用的工具
            // 助手消息
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 获取要调用的工具列表
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            // 输出提示信息
            String result = assistantMessage.getText();
            log.info(getName() + "的思考：" + result);
            log.info(getName() + "选择了" + toolCallList.size() + "个工具来使用");
            String toolCallInfo = toolCallList.stream()
                    .map(toolCall -> String.format("工具名称：%s，工具参数：%s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));
            log.info(getName() + "调用的工具信息如下：\n" + toolCallInfo);
            // 如果不需要调用工具，返回 false
            if (toolCallInfo.isEmpty()) {
                // 只有不调用工具时，才需要手动记录助手消息
                getMessageList().add(assistantMessage);
                return false;
            } else {
                // 需要调用工具时，无需记录助手消息，因为调用工具时会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "的思考过程遇到了问题：" + e.getMessage());
            getMessageList().add(new AssistantMessage("处理时遇到了错误：" + e.getMessage()));
            return false;
        }

    }

    /**
     * 执行工具调用并处理结果
     *
     * @return 执行行动的结果
     */
    @Override
    public String act() {
        // 校验工具响应是否包含需要调用的工具
        if (!toolCallResponse.hasToolCalls()) {
            return "不需要调用工具";
        }
        // 调用工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallManager.executeToolCalls(prompt, toolCallResponse);
        // 记录消息上下文，conversationHistory 已经包含了助手消息和工具调用结果
        List<Message> conversationHistory = toolExecutionResult.conversationHistory();
        setMessageList(conversationHistory);
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(conversationHistory);
        // 判断是否调用了终止工具
        boolean doTerminateToolCalled = toolResponseMessage.getResponses().stream()
                .anyMatch(response -> response.name().equals("doTerminate"));
        if (doTerminateToolCalled) {
            setAgentState(AgentState.FINISHED);
        }
        // 记录当前工具的调用结果
        String result = toolResponseMessage.getResponses().stream()
                .map(response -> "工具 " + response.name() + "调用成功了，调用结果为：" + response.responseData())
                .collect(Collectors.joining(("\n")));
        log.info(getName() + "的调用结果：" + result);
        return result;
    }
}
