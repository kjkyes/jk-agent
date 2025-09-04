package com.xk.jkagent.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
@Configuration
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    /**
     * 带有关键词检索功能的向量存储
     * @param dashScopeEmbeddingModel
     * @return
     */
    @Bean
    VectorStore loveAppVectorStore(DashScopeEmbeddingModel dashScopeEmbeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        List<Document> documents = loveAppDocumentLoader.loadDocument();
        // 带有关键词增强的向量存储功能
        List<Document> documents_withKeywords = myKeywordEnricher.enrichDocuments(documents);
        vectorStore.add(documents_withKeywords);
        return vectorStore;
    }

}
