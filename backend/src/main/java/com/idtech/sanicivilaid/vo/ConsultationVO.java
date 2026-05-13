package com.idtech.sanicivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 法律咨询响应 VO
 * 向前端返回咨询内容，并携带彝文显示字体信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationVO {

    private Long id;

    private Long userId;

    private String questionCn;

    private String questionSani;

    private String answerCn;

    private String answerSani;

    private Integer status;

    private Instant createTime;

    /**
     * 彝文显示字体
     * "Yi Script" —— 本地数据库 FAQ 来源
     * "Microsoft Yi Baiti" —— AI 回答来源
     */
    private String saniFont;
}
