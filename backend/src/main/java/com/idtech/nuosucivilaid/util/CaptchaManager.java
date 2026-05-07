package com.idtech.nuosucivilaid.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public final class CaptchaManager {

    // 验证码存储缓存（captchaId -> 验证码）
    private final Map<String, String> captchaCache = new ConcurrentHashMap<>();

    // 验证码过期时间（2分钟）
    private static final long CAPTCHA_EXPIRATION_MINUTES = 2;

    // 定时清理任务
    private final ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    public CaptchaManager() {
        // 每10分钟清理一次过期验证码
        cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredCaptcha, 10, 10, TimeUnit.MINUTES);
    }

    /**
     * 生成验证码ID并存储验证码
     */
    public String generateCaptcha(String captchaCode) {
        String captchaId = UUID.randomUUID().toString();
        captchaCache.put(captchaId, captchaCode);

        // 设置过期时间（2分钟后自动删除）
        cleanupScheduler.schedule(() -> {
            String removed = captchaCache.remove(captchaId);
            if (removed != null) {
                log.debug("验证码过期自动清理，captchaId: {}", captchaId);
            }
        }, CAPTCHA_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        log.debug("验证码已生成并存储，captchaId: {}, 当前活跃数量: {}", captchaId, captchaCache.size());
        return captchaId;
    }

    /**
     * 验证验证码
     */
    public boolean verifyCaptcha(String captchaId, String userInput) {
        if (captchaId == null || userInput == null) {
            log.warn("验证码校验失败，参数为空");
            return false;
        }

        String storedCaptcha = captchaCache.get(captchaId);
        if (storedCaptcha == null) {
            log.warn("验证码校验失败，验证码不存在或已过期，captchaId: {}", captchaId);
            return false; // 验证码不存在或已过期
        }

        // 验证成功后移除验证码（一次性使用）
        captchaCache.remove(captchaId);

        boolean result = storedCaptcha.equalsIgnoreCase(userInput);
        if (result) {
            log.debug("验证码校验成功，captchaId: {}", captchaId);
        } else {
            log.warn("验证码校验失败，用户输入错误，captchaId: {}", captchaId);
        }
        return result;
    }

    /**
     * 清理过期验证码
     */
    private void cleanupExpiredCaptcha() {
        // 由于我们使用了定时任务自动删除，这里主要是清理意外残留的验证码
        // 简单的清理：如果缓存过大，清理部分旧数据
        int sizeBefore = captchaCache.size();
        if (sizeBefore > 1000) {
            captchaCache.clear();
            log.info("验证码缓存数量超过阈值，执行全量清理，清理前数量: {}", sizeBefore);
        } else if (sizeBefore > 0) {
            log.debug("验证码定时清理任务执行，当前活跃验证码数量: {}", sizeBefore);
        }
    }

    /**
     * 获取当前活跃的验证码数量（用于监控）
     */
    public int getActiveCaptchaCount() {
        return captchaCache.size();
    }
}
