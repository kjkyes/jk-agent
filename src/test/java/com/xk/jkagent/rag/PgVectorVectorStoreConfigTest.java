package com.xk.jkagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class PgVectorVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorVectorStore;

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;
    @Test
    void pgVectorVectorStore() {
        List<Document> documents = List.of(
                new Document("小康是谁啊？我记得他的GitHub地址为：https://github.com/kjkyes", Map.of("meta1", "meta1")),
                new Document("我听说小康人很好，是个研究生，目标互联网大厂"),
                new Document("我笑那**小儿豺狼心肠", Map.of("meta2", "meta2"))
        );
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("小康的github地址是啥啊").topK(3).build());
        Assertions.assertNotNull(results);
    }
}