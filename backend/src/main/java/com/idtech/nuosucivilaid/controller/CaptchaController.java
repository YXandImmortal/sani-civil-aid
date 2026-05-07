package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.service.CaptchaService;
import com.idtech.nuosucivilaid.vo.CaptchaVO;
import com.idtech.nuosucivilaid.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        CaptchaVO captchaVO = captchaService.generateCaptcha();
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
        boolean valid = captchaService.verifyCaptcha(captchaId, userInput);
        return Result.success(valid);
    }
}
