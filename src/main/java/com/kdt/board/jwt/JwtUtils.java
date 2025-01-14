package com.kdt.board.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 생성 및 검증 유틸리티 클래스
 */
public class JwtUtils {

    // 서명에 사용할 SecretKey
    private static final String SECRET_KEY = "YourSecretKeyForJWTGenerationYourSecretKey"; // 256비트 이상의 키 사용
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // 토큰 유효 시간 (Access Token: 30분)
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;

    // JWT 생성
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims) // 사용자 정보 클레임
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 (HMAC SHA256)
                .compact();
    }

    // JWT 검증 및 클레임 반환
    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 서명 키 설정
                .build()
                .parseClaimsJws(token) // 유효성 검증 및 파싱
                .getBody();
    }

    // 토큰 만료 여부 확인
    public static boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new Date());
    }
}
