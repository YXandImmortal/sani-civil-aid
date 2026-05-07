package com.idtech.nuosucivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO {

    /**
     * 验证码唯一标识
     */
    private String captchaId;

    /**
     * 验证码图片Base64字符串（包含data:image/png;base64,前缀）
     */
    private String imageBase64;
}
