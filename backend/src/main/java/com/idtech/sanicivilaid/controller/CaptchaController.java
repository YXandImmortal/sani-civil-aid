package com.idtech.sanicivilaid.controller;

import com.idtech.sanicivilaid.service.CaptchaService;
import com.idtech.sanicivilaid.vo.CaptchaVO;
import com.idtech.sanicivilaid.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取图形验证码
     *
     * @return 验证码图片Base64及唯一标识
     */
    @GetMapping("/generate")
    public Result<CaptchaVO> generateCaptcha() {
        log.debug("生成图形验证码请求");
        CaptchaVO captchaVO = captchaService.generateCaptcha();
        log.debug("生成图形验证码成功，captchaId: {}", captchaVO.getCaptchaId());
        return Result.success(captchaVO);
    }

    /**
     * 校验验证码（供前端预校验或调试使用）
     *
     * @param captchaId 验证码标识
     * @param userInput 用户输入
     * @return 校验结果
     */
    @PostMapping("/verify")
    public Result<Boolean> verifyCaptcha(@RequestParam String captchaId, @RequestParam String userInput) {
        log.debug("验证码校验请求，captchaId: {}", captchaId);
        boolean valid = captchaService.verifyCaptcha(captchaId, userInput);
        log.debug("验证码校验结果，captchaId: {}, 结果: {}", captchaId, valid);
        return Result.success(valid);
    }
}
