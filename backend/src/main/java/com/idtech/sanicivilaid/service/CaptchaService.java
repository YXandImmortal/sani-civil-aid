package com.idtech.sanicivilaid.service;

import com.idtech.sanicivilaid.vo.CaptchaVO;

public interface CaptchaService {

    /**
     * 生成图形验证码
     *
     * @return 验证码信息（包含验证码ID和Base64图片）
     */
    CaptchaVO generateCaptcha();

    /**
     * 校验验证码
     *
     * @param captchaId 验证码标识
     * @param userInput 用户输入的验证码
     * @return 校验是否通过
     */
    boolean verifyCaptcha(String captchaId, String userInput);
}
