package com.kdt.board.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * JWT를 블랙리스트에 추가
     * @param token 블랙리스트에 추가할 JWT
     * @param expirationTime JWT의 만료 시간 (초 단위)
     */
    public void blacklistToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.SECONDS);
        System.out.println("블랙리스트에 추가된 토큰: " + token);
        System.out.println("남은 유효 시간(초): " + expirationTime);
    }

    /**
     * JWT가 블랙리스트에 있는지 확인
     * @param token 확인할 JWT
     * @return 블랙리스트 여부
     */
    public boolean isTokenBlacklisted(String token) {
        boolean isBlacklisted = redisTemplate.hasKey(token);
        System.out.println("토큰 블랙리스트 여부 확인: " + token + " -> " + isBlacklisted);
        return isBlacklisted;
    }
}
