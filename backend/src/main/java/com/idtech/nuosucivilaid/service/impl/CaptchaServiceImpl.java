package com.idtech.nuosucivilaid.service.impl;

import com.idtech.nuosucivilaid.service.CaptchaService;
import com.idtech.nuosucivilaid.util.CaptchaManager;
import com.idtech.nuosucivilaid.vo.CaptchaVO;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private CaptchaManager captchaManager;

    @Override
    public CaptchaVO generateCaptcha() {
        SpecCaptcha captcha = new SpecCaptcha(130, 32, 4);
        captcha.setCharType(SpecCaptcha.TYPE_DEFAULT);
        String code = captcha.text();
        String base64 = captcha.toBase64();

        String captchaId = captchaManager.generateCaptcha(code);
        log.debug("生成图形验证码成功，captchaId: {}", captchaId);

        return CaptchaVO.builder()
                .captchaId(captchaId)
                .imageBase64(base64)
                .build();
    }

    @Override
    public boolean verifyCaptcha(String captchaId, String userInput) {
        return captchaManager.verifyCaptcha(captchaId, userInput);
    }
}
