package com.xk.jkagent.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class PgVectorVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, DashScopeEmbeddingModel dashScopeEmbeddingModel) {
        PgVectorStore pgVectorStore = PgVectorStore.builder(jdbcTemplate, dashScopeEmbeddingModel)
                .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .build();
        // 1. 判断表里是否已有记录
        Long cnt = jdbcTemplate.queryForObject(
                "select count(*) from public.vector_store", Long.class);
        if (cnt > 0) {
            // 已有文档，直接返回，不再写入
            return pgVectorStore;
        }

        // 2. 表为空，可以加载文档
        List<Document> documents = loveAppDocumentLoader.loadDocument();
        pgVectorStore.add(documents);
        return pgVectorStore;
    }
}
