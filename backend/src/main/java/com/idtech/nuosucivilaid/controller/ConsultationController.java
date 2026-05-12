package com.idtech.nuosucivilaid.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.json.JSONUtil;
import com.idtech.nuosucivilaid.entity.BizCivilFaq;
import com.idtech.nuosucivilaid.entity.Consultation;
import com.idtech.nuosucivilaid.repository.BizCivilFaqRepository;
import com.idtech.nuosucivilaid.repository.ConsultationRepository;
import com.idtech.nuosucivilaid.exception.BusinessException;
import com.idtech.nuosucivilaid.service.AuthService;
import com.idtech.nuosucivilaid.vo.AiCivilArticleVO;
import com.idtech.nuosucivilaid.vo.ConsultationVO;
import com.idtech.nuosucivilaid.vo.Result;
import com.idtech.nuosucivilaid.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/civil/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private BizCivilFaqRepository bizCivilFaqRepository;

    @Autowired
    private AuthService authService;

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 提交新咨询
    @PostMapping("/submit")
    public Result<ConsultationVO> submit(@RequestBody Consultation consultation) {
        UserInfoVO currentUser = null;
        try {
            currentUser = authService.getCurrentUserInfo();
            consultation.setUserId(currentUser.getId());
        } catch (Exception e) {
            // 匿名用户提交，userId 保持 null
            consultation.setUserId(null);
        }
        consultation.setCreateTime(Instant.now());
        consultation.setStatus(0);
        consultation.setIsDeleted(false);
        consultation.setNuosuFont("");

        consultationRepository.save(consultation);
        String userLabel = currentUser != null ? currentUser.getUsername() : "匿名用户";
        log.info("用户 {} 提交了新的法律咨询", userLabel);

        // 尝试从 FAQ 中搜索答案
        List<BizCivilFaq> faqList = null;
        String questionCn = consultation.getQuestionCn();
        String questionNuosu = consultation.getQuestionNuosu();

        if (StrUtil.isNotBlank(questionCn)) {
            // 使用 hutool 进行中文分词后搜索
            List<String> keywords = segmentChinese(questionCn);
            for (String keyword : keywords) {
                faqList = bizCivilFaqRepository.searchByCnKeyword(keyword);
                if (CollUtil.isNotEmpty(faqList)) {
                    break;
                }
            }
        } else if (StrUtil.isNotBlank(questionNuosu)) {
            // 中文提问为空，使用彝文进行模糊搜索
            faqList = bizCivilFaqRepository.searchByNuosuKeyword(questionNuosu);
        }

        if (CollUtil.isNotEmpty(faqList)) {
            // 数据库中搜索到答案
            BizCivilFaq faq = faqList.get(0);
            consultation.setAnswerCn(faq.getAnswerCn());
            consultation.setAnswerNuosu(faq.getAnswerNuosu());
            consultation.setStatus(1);
            consultation.setNuosuFont("Yi Script");
            consultationRepository.save(consultation);
            log.info("用户 {} 的咨询从 FAQ 中获取到答案", userLabel);
            return Result.success(toVO(consultation));
        }

        // 数据库中没有相关记录，调用 AI
        String searchKeyword = StrUtil.isNotBlank(questionCn) ? questionCn : questionNuosu;
        AiCivilArticleVO aiResult = callAi(searchKeyword);
        if (aiResult != null) {
            consultation.setAnswerCn(aiResult.getContentCn());
            consultation.setAnswerNuosu(aiResult.getContentNuosu());
            consultation.setStatus(1);
            consultation.setNuosuFont("Microsoft Yi Baiti");
            consultationRepository.save(consultation);
            log.info("用户 {} 的咨询通过 AI 获取到答案", userLabel);
            return Result.success(toVO(consultation));
        } else {
            log.warn("用户 {} 的咨询 AI 未能返回结果", userLabel);
        }

        consultationRepository.save(consultation);
        return Result.success(toVO(consultation));
    }

    /**
     * 使用 hutool 进行中文分词
     */
    private List<String> segmentChinese(String text) {
        List<String> keywords = new ArrayList<>();
        try {
            TokenizerEngine engine = TokenizerUtil.createEngine();
            var result = engine.parse(text);
            for (Word word : result) {
                String w = word.getText();
                if (StrUtil.isNotBlank(w) && w.length() > 1) {
                    keywords.add(w);
                }
            }
        } catch (Exception e) {
            log.warn("hutool 中文分词引擎不可用，使用整句作为关键词: {}", e.getMessage());
            keywords.add(text);
        }
        if (keywords.isEmpty()) {
            keywords.add(text);
        }
        return keywords;
    }

    /**
     * 调用 DeepSeek AI
     */
    private AiCivilArticleVO callAi(String keyword) {
        log.info("正在请求 DeepSeek AI 检索: {}", keyword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

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
            return JSONUtil.toBean(content, AiCivilArticleVO.class);
        } catch (Exception e) {
            log.error("AI 检索失败", e);
            return null;
        }
    }

    // 删除咨询记录（逻辑删除）
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        UserInfoVO currentUser = authService.getCurrentUserInfo();
        Consultation consultation = consultationRepository.findById(id).orElse(null);
        if (consultation == null || Boolean.TRUE.equals(consultation.getIsDeleted())) {
            throw BusinessException.consultationNotFound();
        }
        if (!consultation.getUserId().equals(currentUser.getId())) {
            throw BusinessException.consultationNoPermission();
        }
        consultation.setIsDeleted(true);
        consultationRepository.save(consultation);
        log.info("用户 {} 删除了咨询记录 {}", currentUser.getUsername(), id);
        return Result.success(null);
    }

    // 获取我的咨询历史
    @GetMapping("/my-list")
    public Result<List<ConsultationVO>> getMyList() {
        UserInfoVO currentUser = authService.getCurrentUserInfo();
        List<Consultation> list = consultationRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(currentUser.getId());
        List<ConsultationVO> voList = list.stream()
                .map(this::toVO)
                .toList();
        return Result.success(voList);
    }

    /**
     * 将 Consultation 实体转换为 ConsultationVO
     */
    private ConsultationVO toVO(Consultation consultation) {
        return ConsultationVO.builder()
                .id(consultation.getId())
                .userId(consultation.getUserId())
                .questionCn(consultation.getQuestionCn())
                .questionNuosu(consultation.getQuestionNuosu())
                .answerCn(consultation.getAnswerCn())
                .answerNuosu(consultation.getAnswerNuosu())
                .status(consultation.getStatus())
                .createTime(consultation.getCreateTime())
                .nuosuFont(consultation.getNuosuFont())
                .build();
    }
}
