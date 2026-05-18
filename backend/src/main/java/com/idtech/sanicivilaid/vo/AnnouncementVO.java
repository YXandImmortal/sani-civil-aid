package com.idtech.sanicivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementVO {

    /**
     * 当前系统版本号
     */
    private String currentVersion;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否需要向当前用户展示公告
     * <p>true：用户未读或版本未更新，需要展示</p>
     * <p>false：用户已读或未登录无需展示</p>
     */
    private Boolean needShow;
}
