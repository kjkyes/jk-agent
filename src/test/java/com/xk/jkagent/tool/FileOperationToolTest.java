package com.xk.jkagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String s = fileOperationTool.readFile("GitHub账号.txt");
        Assertions.assertNotNull(s);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String s = fileOperationTool.writeFile("GitHub账号.txt", "小康的GitHub账号是：https://github.com/kjkyes");
        Assertions.assertNotNull(s);
    }
}