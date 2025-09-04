package com.xk.jkagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RAG实战之文档过滤和检索-多查询扩展
 */
@Component
public class MultiQueryExpanderDemo {

    private ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel dashscopeChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }

    public List<Query> expand(Query query) {
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .includeOriginal(false)
                .build();
        List<Query> queries = multiQueryExpander.expand(query);
        return queries;
    }

}
