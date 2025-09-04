package com.xk.jkagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档加载器，将目标路径文档读取出来
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

/**
 * Loads all markdown documents from the document directory on the classpath.
 * This method reads all .md files from the classpath:document/ directory,
 * processes them using MarkdownDocumentReader with specific configuration,
 * and returns a list of Document objects.
 *
 * @return List<Document> A list containing all loaded documents, or an empty list if loading fails
 */
    public List<Document> loadDocument() {
        // List to store all loaded documents
        List<Document> allDocument = new ArrayList<>();
        try {
            // Get all resources matching the pattern "classpath:document/*.md"
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            // Iterate through each resource (markdown file)
            for (Resource resource : resources) {
                // Extract the filename from the resource
                String fileName = resource.getFilename();
                String status = fileName.substring(fileName.length() - 6, fileName.length() - 4);
                // Build configuration for the MarkdownDocumentReader
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)    // Include horizontal rules as documents
                        .withIncludeCodeBlock(false)              // Exclude code blocks
                        .withIncludeBlockquote(false)            // Exclude blockquotes
                        .withAdditionalMetadata("filename", fileName) // Add filename as metadata
                        .withAdditionalMetadata("status", status) // Add status as metadata
                        .build();
                // Create reader with resource and config
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                // Read documents from the markdown file and add to collection
                allDocument.addAll((reader.get()));
            }

        } catch (IOException e) {
            // Log error if document loading fails
            log.error("加载文档失败", e);
        }
        return allDocument;
    }

}
