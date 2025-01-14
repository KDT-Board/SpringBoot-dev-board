package com.kdt.board.user.controller;

import com.kdt.board.jwt.JwtUtils;
import com.kdt.board.user.dto.UserJoinRequest;
import com.kdt.board.user.dto.UserLoginRequest;
import com.kdt.board.user.dto.UserLoginResponse;
import com.kdt.board.user.service.LogoutService;
import com.kdt.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final LogoutService logoutService; // LogoutService 주입
    private final UserService userService; // UserService 주입

    /**
     * 회원가입 API
     * @param userJoinRequest 회원가입 요청 데이터
     * @return 성공 메시지
     */
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserJoinRequest userJoinRequest) {
        try {
            userService.registerUser(userJoinRequest);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 로그인 API
     * @param userLoginRequest 로그인 요청 데이터
     * @return JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        System.out.println("로그인 요청 데이터: " + userLoginRequest);
        try {
            String token = userService.login(userLoginRequest);
            System.out.println("로그인 성공, JWT 발급: " + token);
            return ResponseEntity.ok(new UserLoginResponse(token));
        } catch (IllegalArgumentException e) {
            System.out.println("로그인 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(e.getMessage()));
        }
    }


    /**
     * 로그아웃 API
     * @param request 클라이언트 요청
     * @return 성공 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 헤더에서 JWT 추출
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 요청입니다.");
        }

        String token = header.substring(7); // "Bearer " 제거

        try {
            // JWT 만료 시간 추출
            var claims = JwtUtils.validateToken(token);
            long expirationTime = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;

            // JWT를 블랙리스트에 추가
            logoutService.blacklistToken(token, expirationTime);
            return ResponseEntity.ok("로그아웃 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }
}
