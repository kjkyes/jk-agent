package com.xk.jkagent.agent;

/**
 * ReAct（Reasoning and Acting）模式的 agent抽象类
 * 实现了思考-行动的循环模式
 */
public abstract class ReActAgent extends BaseAgent {

    /**
     * 思考方法，根据当前状态和消息上下文决定是否进行下一步的行动
     *
     * @return true表示需要继续行动，false表示结束行动
     */
    public abstract boolean think();

    /**
     * 行动方法，根据当前状态和消息上下文执行相应的操作
     *
     * @return 操作的结果
     */
    public abstract String act();

    @Override
    public String step() {
        try {
            if (think()) {
                return act();
            }else {
                return "思考完成，无需额外行动";
            }
        } catch (Exception e) {
            e.getStackTrace();
            return "步骤执行失败：" + e.getMessage();
        }
    }
}
