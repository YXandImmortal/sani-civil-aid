package com.idtech.sanicivilaid.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmbeddingService {

    @Value("${deepseek.embedding.api-url}")
    private String embeddingApiUrl;

    @Value("${deepseek.embedding.model}")
    private String embeddingModel;

    @Value("${deepseek.embedding.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public float[] embed(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", embeddingModel);
        body.put("input", text);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(embeddingApiUrl, entity, String.class);
            JSONObject json = JSONUtil.parseObj(response.getBody());
            JSONArray dataArray = json.getJSONArray("data");
            JSONObject first = dataArray.getJSONObject(0);
            JSONArray embeddingArray = first.getJSONArray("embedding");

            float[] result = new float[embeddingArray.size()];
            for (int i = 0; i < embeddingArray.size(); i++) {
                result[i] = embeddingArray.getFloat(i);
            }
            return result;
        } catch (Exception e) {
            log.error("Embedding API 调用失败: {}", e.getMessage());
            throw new RuntimeException("Embedding 生成失败", e);
        }
    }
}
