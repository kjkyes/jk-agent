package com.xk.jkagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义查询增强器
 */
@Component
@Slf4j
public class LoveAppContextualQueryAugmenterAdvisorFactory {
    public static QueryAugmenter createInstance(String keyword) {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该按照下面的示例回答：
                抱歉，我只能回答{keyword}相关的问题。
                """, Map.of("keyword", keyword));
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
    }

}
