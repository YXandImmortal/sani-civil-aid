package com.idtech.sanicivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 民法典检索结果 VO
 * 封装 DeepSeek 返回的汉彝双语解释内容
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiCivilArticleVO {

    /**
     * 汉文解释
     */
    private String contentCn;

    /**
     * 规范彝文翻译
     */
    private String contentSani;
}
