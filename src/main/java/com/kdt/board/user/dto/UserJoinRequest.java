package com.kdt.board.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinRequest {

    // 이메일 형식 검증 : test@example.com
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.com$", message = "유효한 이메일 주소여야 합니다.")
    private String email;

    // 비밀번호 검증: 영문, 숫자, 기호 포함 최소 8자
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "비밀번호는 영문, 숫자, 기호를 포함한 최소 8자리여야 합니다.")
    private String password;

    // 이름 검증: 영어 4글자 이상 또는 한글 2글자 이상
    @Pattern(regexp = "^[A-Za-z]{4,}$|^[가-힣]{2,}$", message = "이름은 영어 4글자 이상 또는 한글 2글자 이상이어야 합니다.")
    private String name;

}