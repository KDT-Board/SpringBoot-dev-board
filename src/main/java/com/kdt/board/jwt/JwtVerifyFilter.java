package com.kdt.board.jwt;

import com.kdt.board.user.service.LogoutService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT를 검증하고 인증 정보를 설정하는 필터
 */
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final LogoutService logoutService;

    // LogoutService 주입받기
    public JwtVerifyFilter(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("JWT Header is missing or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // "Bearer " 제거
        try {
            // JWT 검증
            JwtUtils.validateToken(token);
            System.out.println("JWT validated successfully: " + token);

            // SecurityContext 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    null, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
