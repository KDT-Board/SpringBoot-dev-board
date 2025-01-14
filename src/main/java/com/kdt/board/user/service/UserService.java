package com.kdt.board.user.service;


import com.kdt.board.jwt.JwtUtils;
import com.kdt.board.user.User;
import com.kdt.board.user.UserRepository;
import com.kdt.board.user.UserRole;
import com.kdt.board.user.dto.UserJoinRequest;
import com.kdt.board.user.dto.UserLoginRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; // 사용자 데이터베이스 접근
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화

    public String registerUser(UserJoinRequest userJoinRequest) {
        // 이메일 중복 확인
        Optional<User> existingUser = userRepository.findByEmail(userJoinRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userJoinRequest.getPassword());

        // 사용자 엔티티 생성 및 저장
        User newUser = User.builder()
                .email(userJoinRequest.getEmail())
                .password(encodedPassword)
                .name(userJoinRequest.getName())
                .role(UserRole.USER) // 기본 사용자 권한 설정
                .build();

        userRepository.save(newUser);

        return "회원가입이 성공적으로 완료되었습니다.";
    }

    public String login(UserLoginRequest userLoginRequest) {
        // 사용자 조회
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성
        return JwtUtils.generateToken(Map.of(
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }
}