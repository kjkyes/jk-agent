package com.xk.jkagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void doResourceDownload() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://image.so.com/z?a=viewPage&ch=wallpaper&src=home_wallpaper" +
                "&sitename=%E8%A7%86%E8%A7%89%E4%B8%AD%E5%9B%BD&ancestor=list#id=c6d681ef58cf9e01e8954869f4a44131" +
                "&grpid=c04db9925a476731000d4c995cc01694&currsn=120&prevsn=90";
        String fileName = "picture.png";
        String result = resourceDownloadTool.doResourceDownload(url, fileName);
        Assertions.assertNotNull(result);
    }
}