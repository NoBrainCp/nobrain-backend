package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserEmailDuplicationException;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UsernameDuplicationException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.global.security.Encryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserSignServiceTest {

    @Autowired private UserSignService userSignService;
    @Autowired private UserRepository userRepository;
    @Autowired private Encryptor encryptor;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void signup() {
        String name = "test";
        String email = "test@email.com";
        String password = "testPassword";
        UserRequest.SignUp signUpDto = createSignUpDto(name, email, password, password);

        Long userId = userSignService.signUp(signUpDto);
        User user = userRepository.findById(userId).get();

        Assertions.assertThat(signUpDto.getName()).isEqualTo(user.getName());
        Assertions.assertThat(signUpDto.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(encryptor.isMatch(password, user.getPassword())).isEqualTo(true);
    }

    @Test
    @DisplayName("회원가입 - 중복 이름 실패")
    void signup_username_duplication() {
        String name = "test";
        String email = "test@email.com";
        String password = "testPassword";
        String passwordCheck = "testPassword";
        UserRequest.SignUp signUpDto = createSignUpDto(name, email, password, passwordCheck);
        userSignService.signUp(signUpDto);

        UserRequest.SignUp signUpDto2 = createSignUpDto(name, "newEmail", password, passwordCheck);

        Assertions.assertThatThrownBy(() -> userSignService.signUp(signUpDto2))
                .isInstanceOf(UsernameDuplicationException.class);
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일 실패")
    void signup_email_duplication() {
        String name = "test";
        String email = "test@email.com";
        String password = "testPassword";
        String passwordCheck = "testPassword";
        UserRequest.SignUp signUpDto1 = createSignUpDto(name, email, password, passwordCheck);
        userSignService.signUp(signUpDto1);

        UserRequest.SignUp signUpDto2 = createSignUpDto("newName", email, password, passwordCheck);

        Assertions.assertThatThrownBy(() -> userSignService.signUp(signUpDto2))
                .isInstanceOf(UserEmailDuplicationException.class);
    }

    @Test
    @DisplayName("회원가입 - 비밀번호 불일치 실패")
    void signup_password_not_correct() {
        String name = "test";
        String email = "test@email.com";
        String password = "testPassword";
        String passwordCheck = "newPassword";
        UserRequest.SignUp signUpDto = createSignUpDto(name, email, password, passwordCheck);

        Assertions.assertThatThrownBy(() -> userSignService.signUp(signUpDto))
                .isInstanceOf(UserNotCorrectPasswordException.class);
    }

    private UserRequest.SignUp createSignUpDto(String name, String email, String password, String passwordCheck) {
        UserRequest.SignUp signUpDto = new UserRequest.SignUp();
        signUpDto.setName(name);
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        signUpDto.setPasswordCheck(passwordCheck);
        return signUpDto;
    }
}