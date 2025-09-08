package com.xk.jkagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "xk的智能体项目.pdf";
        String content = "xk的智能体项目：https://github.com/kjkyes/jk-agent";
        String result = tool.generatePDF(fileName, content);
        Assertions.assertNotNull(result);
    }
}
