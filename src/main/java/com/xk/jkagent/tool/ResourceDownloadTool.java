package com.xk.jkagent.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.xk.jkagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * 资源下载工具（根据链接下载文件到本地）
 */
public class ResourceDownloadTool {
    @Tool(description = "Download file from url to local path")
    public String doResourceDownload(@ToolParam(description = "The required url") String url,
                                     @ToolParam(description = "The path should be saved") String fileName) {
        String fileDir = FileConstant.SAVE_FILE_PATH + "/download";
        String filePath = fileDir + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 下载文件
            HttpUtil.downloadFile(url, new File(filePath));
            return "Resource downloaded successfully:" + filePath;
        } catch (Exception e) {
            return "Resource download failed:" + e.getMessage();
        }

    }
}
