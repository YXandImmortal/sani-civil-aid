package com.idtech.sanicivilaid.service;

import com.idtech.sanicivilaid.vo.AnnouncementVO;
import com.idtech.sanicivilaid.vo.SystemInfoResponse;

public interface SystemInfoService {

    SystemInfoResponse getSystemInfo();

    /**
     * 获取系统更新公告信息
     *
     * @return 公告内容以及当前用户是否需要展示
     */
    AnnouncementVO getAnnouncement();

    /**
     * 将当前登录用户的公告标记为已读
     */
    void markAnnouncementRead();
}
