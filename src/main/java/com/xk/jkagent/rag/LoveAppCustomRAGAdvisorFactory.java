package com.xk.jkagent.rag;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

/**
 * 自定义LoveApp检索增强器
 */
@Component
public class LoveAppCustomRAGAdvisorFactory {

    public static Advisor createLoveAppRagAdvisor(VectorStore vectorStore, String status) {
        Filter.Expression filterExpression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .filterExpression(filterExpression)
                .vectorStore(vectorStore)
                .similarityThreshold(0.5)
                .topK(3)
                .build();
        return RetrievalAugmentationAdvisor.builder()
                // 检索增强器
                .documentRetriever(documentRetriever)
                // 查询增强器
                .queryAugmenter(LoveAppContextualQueryAugmenterAdvisorFactory.createInstance(status))
                .build();
    }

}
