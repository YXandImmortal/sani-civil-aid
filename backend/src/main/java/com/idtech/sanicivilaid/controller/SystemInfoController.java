package com.idtech.sanicivilaid.controller;

import com.idtech.sanicivilaid.service.SystemInfoService;
import com.idtech.sanicivilaid.vo.AnnouncementVO;
import com.idtech.sanicivilaid.vo.Result;
import com.idtech.sanicivilaid.vo.SystemInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system")
public class SystemInfoController {

    @Autowired
    private SystemInfoService systemInfoService;

    /**
     * 获取系统基本信息、技术栈、开发团队及相关链接
     *
     * @return 系统综合信息
     */
    @GetMapping("/info")
    public Result<SystemInfoResponse> getSystemInfo() {
        log.debug("获取系统信息请求");
        SystemInfoResponse response = systemInfoService.getSystemInfo();
        log.debug("获取系统信息成功");
        return Result.success(response);
    }

    /**
     * 获取系统更新公告
     *
     * @return 公告内容，包含是否需要向当前用户展示的标志
     */
    @GetMapping("/announcement")
    public Result<AnnouncementVO> getAnnouncement() {
        log.debug("获取系统更新公告请求");
        AnnouncementVO announcement = systemInfoService.getAnnouncement();
        log.debug("获取系统更新公告成功，needShow={}", announcement.getNeedShow());
        return Result.success(announcement);
    }

    /**
     * 标记当前登录用户已阅读更新公告
     *
     * @return 操作结果
     */
    @PostMapping("/announcement/read")
    public Result<Void> markAnnouncementRead() {
        log.debug("标记更新公告已读请求");
        systemInfoService.markAnnouncementRead();
        log.debug("标记更新公告已读成功");
        return Result.success();
    }
}
