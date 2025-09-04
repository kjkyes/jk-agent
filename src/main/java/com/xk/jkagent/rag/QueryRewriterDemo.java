package com.xk.jkagent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * RAG实战之文档过滤和检索-查询重写
 */
@Component
public class QueryRewriterDemo {
    private final QueryTransformer queryTransformer;

    public QueryRewriterDemo(ChatModel dashscopeChatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(dashscopeChatModel);
        queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
    }

    /**
     * 执行查询重写
     * @param prompt
     * @return
     */
    public String doQueryRewrite(String prompt) {
        // 执行提示词重写
        Query rewrittenQuery = queryTransformer.transform(new Query(prompt));
        // 返回重写后的查询文本
        return rewrittenQuery.text();
    }
}
