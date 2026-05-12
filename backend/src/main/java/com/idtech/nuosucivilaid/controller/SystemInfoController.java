package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.service.SystemInfoService;
import com.idtech.nuosucivilaid.vo.Result;
import com.idtech.nuosucivilaid.vo.SystemInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
