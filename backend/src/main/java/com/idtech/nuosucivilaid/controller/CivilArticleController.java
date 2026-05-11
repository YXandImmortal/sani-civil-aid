package com.idtech.nuosucivilaid.controller;

import cn.hutool.json.JSONUtil;
import com.idtech.nuosucivilaid.entity.CivilArticle;
import com.idtech.nuosucivilaid.repository.CivilArticleRepository;
import com.idtech.nuosucivilaid.vo.AiCivilArticleVO;
import com.idtech.nuosucivilaid.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/civil/article")
public class CivilArticleController {

    @Autowired
    private CivilArticleRepository articleRepository;

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/search")
    public Result<List<CivilArticle>> search(@RequestParam String keyword) {
        log.info("本地数据库搜索: {}", keyword);
        return Result.success(articleRepository.searchArticles(keyword));
    }

    @GetMapping("/category/{id}")
    public Result<List<CivilArticle>> getByCategory(@PathVariable Long id) {
        return Result.success(articleRepository.findByCategoryIdAndIsDeletedFalseOrderByArticleNumAsc(id));
    }

    /**
     * AI 检索接口：调用 DeepSeek 大模型，返回汉彝双语解释
     */
    @GetMapping("/ai-search")
    public Result<AiCivilArticleVO> aiSearch(@RequestParam String keyword) {
        log.info("正在请求 DeepSeek AI 检索: {}", keyword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 构建 Prompt，要求 AI 返回汉彝双语
        String prompt = String.format(
                "你是一个专业的中国法律助理。用户查询了民法典相关内容：'%s'。我们的数据库暂未收录。 " +
                        "请根据中华人民共和国民法典提供简洁的解释。 " +
                        "必须包含以下两部分：1. 汉文解释。2. 对应的规范彝文（Nuosu Yi）翻译。 " +
                        "请以 JSON 格式返回，字段名为 contentCn 和 contentNuosu。", keyword);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        requestBody.put("response_format", new HashMap<String, String>() {{
            put("type", "json_object");
        }});

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");

            // 将 AI 返回的 JSON 字符串解析为类型安全的 AIVO
            AiCivilArticleVO aiResult = JSONUtil.toBean(content, AiCivilArticleVO.class);
            return Result.success(aiResult);
        } catch (Exception e) {
            log.error("AI 检索失败", e);
            return Result.error(500, "AI 服务暂时不可用");
        }
    }
}