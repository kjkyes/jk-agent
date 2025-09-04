package com.xk.xkimagesearchmcp.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xk.xkimagesearchmcp.constant.PexelsApiKey;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pexels 图片搜索工具（基于 Hutool）
 */
@Service
public class PexelsSearchTool {

    // 把你在 https://www.pexels.com/api/ 申请到的 token 写这里
    private static final String API_KEY = PexelsApiKey.API_KEY;

    // 搜索封装实体
    public class Photo {
        private int id;
        private String photographer;
        private String originalUrl;

        // getter / setter 省略
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        @Override
        public String toString() {
            return "Photo{id=" + id +
                    ", photographer='" + photographer + '\'' +
                    ", originalUrl='" + originalUrl + '\'' +
                    '}';
        }
    }

    /**
     * 搜索图片
     *
     * @param query       必填搜索关键字
     * @return Photo 列表
     */
    @Tool(description = "search pictures from pexels")
    public List<Photo> searchPhotos(@ToolParam(description = "keywords of the picture") String query) {
        String url = "https://api.pexels.com/v1/search";
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        HttpResponse resp = HttpRequest.get(url)
                .form(params)
                .header("Authorization", API_KEY)
                .timeout(5000)
                .execute();

        if (!resp.isOk()) {
            throw new RuntimeException("HTTP error: " + resp.getStatus());
        }

        JSONObject json = JSONUtil.parseObj(resp.body());
        JSONArray photosJson = json.getJSONArray("photos");
        List<Photo> result = new ArrayList<>();

        for (int i = 0; i < photosJson.size(); i++) {
            JSONObject p = photosJson.getJSONObject(i);
            Photo photo = new Photo();
            photo.setId(p.getInt("id"));
            photo.setPhotographer(p.getStr("photographer"));
            photo.setOriginalUrl(p.getJSONObject("src").getStr("original"));
            result.add(photo);
        }
        return result;
    }

}