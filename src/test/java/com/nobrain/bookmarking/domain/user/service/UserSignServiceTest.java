package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserEmailDuplicationException;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UsernameDuplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class UserSignServiceTest extends ServiceTest {

    @Autowired
    private UserSignService userSignService;

    @Test
    @DisplayName("회원가입 - 성공")
    void signup() {
        UserRequest.SignUp signUpRequest = createSignUpDto(USERNAME, EMAIL, PASSWORD, PASSWORD_CHECK);

        User savedUser = User.builder()
                .id(USER_ID)
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .build();

        given(users.save(any(User.class)))
                .willReturn(savedUser);

        assertThat(userSignService.signUp(signUpRequest)).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("회원가입 - 중복 이름 실패")
    void signup_username_duplication() {
        // given
        UserRequest.SignUp signUpRequest = createSignUpDto(USERNAME, EMAIL, PASSWORD, PASSWORD_CHECK);

        given(users.existsByName(anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> userSignService.signUp(signUpRequest))
                .isInstanceOf(UsernameDuplicationException.class);
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일 실패")
    void signup_email_duplication() {
        // given
        UserRequest.SignUp signUpRequest = createSignUpDto(USERNAME, EMAIL, PASSWORD, PASSWORD_CHECK);

        given(users.existsByName(anyString()))
                .willReturn(false);
        given(users.existsByEmail(anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> userSignService.signUp(signUpRequest))
                .isInstanceOf(UserEmailDuplicationException.class);
    }

    @Test
    @DisplayName("회원가입 - 비밀번호 불일치 실패")
    void signup_password_not_correct() {
        UserRequest.SignUp signUpDto = createSignUpDto(USERNAME, EMAIL, PASSWORD, PASSWORD_CHECK_NOT_SAME);

        assertThatThrownBy(() -> userSignService.signUp(signUpDto))
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