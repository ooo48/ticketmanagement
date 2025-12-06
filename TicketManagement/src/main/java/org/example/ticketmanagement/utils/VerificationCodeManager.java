package org.example.ticketmanagement.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeManager {
    private final Map<String, CodeInfo> codeCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public VerificationCodeManager() {
        // 定时清理过期验证码
        scheduler.scheduleAtFixedRate(this::cleanupExpiredCodes, 1, 1, TimeUnit.HOURS);
    }

    public void saveCode(String email, String code) {
        codeCache.put(email, new CodeInfo(code, System.currentTimeMillis()));
    }

    public boolean verifyCode(String email, String code) {
        CodeInfo codeInfo = codeCache.get(email);
        if (codeInfo == null) {
            return false;
        }

        // 验证码5分钟有效
        if (System.currentTimeMillis() - codeInfo.timestamp > 5 * 60 * 1000) {
            codeCache.remove(email);
            return false;
        }

        boolean result = codeInfo.code.equals(code);
        if (result) {
            codeCache.remove(email);
        }
        return result;
    }

    private void cleanupExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        codeCache.entrySet().removeIf(entry ->
                currentTime - entry.getValue().timestamp > 5 * 60 * 1000);
    }

    private static class CodeInfo {
        String code;
        long timestamp;

        CodeInfo(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
    }
}
