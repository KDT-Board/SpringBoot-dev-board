package com.kdt.board.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 저장, 이메일 조회 가능")
    void givenUser_whenSaveAndFindByEmail_thenShouldReturnUser() {

        // Given: 사용자 데이터가 준비되었을 때
        User user = User.builder()
                .email("test@example.com")
                .password("qwer1234")
                .name("lee")
                .role(UserRole.USER)
                .build();

        // When: 사용자를 저장하고 이메일로 조회할 때
        userRepository.save(user);
        User savedUser = userRepository.findByEmail("test@example.com").orElseThrow();

        // Then: 조회된 사용자의 이름과 저장된 이름이 일치해야 한다
        assertThat(savedUser.getName()).isEqualTo("lee");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
    }
}
