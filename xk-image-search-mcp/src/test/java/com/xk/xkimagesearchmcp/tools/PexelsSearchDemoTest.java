package com.xk.xkimagesearchmcp.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PexelsSearchDemoTest {

    @Resource
    private PexelsSearchTool pexelsSearchTool;

    @Test
    void searchPhotos() {
        List<PexelsSearchTool.Photo> cat = pexelsSearchTool.searchPhotos("cat");
        Assertions.assertNotNull(cat);
    }
}