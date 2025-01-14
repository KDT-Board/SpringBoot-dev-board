package com.kdt.board.config;

import com.kdt.board.jwt.JwtVerifyFilter;
import com.kdt.board.user.service.LogoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final LogoutService logoutService;

    public SecurityConfig(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/signUp").permitAll() // 회원가입 허용
                        .requestMatchers("/api/users/login").permitAll() // 로그인 허용
                        .anyRequest().authenticated() // 다른 요청은 인증 필요
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 비활성화
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화
                .addFilterBefore(new JwtVerifyFilter(logoutService), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

}
