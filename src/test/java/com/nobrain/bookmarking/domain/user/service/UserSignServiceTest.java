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

    private final UserRequest.SignUp userSignupRequest = new UserRequest.SignUp(
            USERNAME,
            EMAIL,
            PASSWORD,
            PASSWORD_CHECK
    );

    @Test
    @DisplayName("회원가입 - 성공")
    void signup() {
        // given
        User savedUser = User.builder()
                .id(USER_ID)
                .name(userSignupRequest.getName())
                .email(userSignupRequest.getEmail())
                .build();

        given(users.save(any(User.class)))
                .willReturn(savedUser);

        // when
        Long actual = userSignService.signUp(userSignupRequest);

        // then
        assertThat(actual).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("회원가입 - 중복 이름 실패")
    void signup_username_duplication() {
        // given
        given(users.existsByName(anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> userSignService.signUp(userSignupRequest))
                .isInstanceOf(UsernameDuplicationException.class);
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일 실패")
    void signup_email_duplication() {
        // given
        given(users.existsByName(anyString()))
                .willReturn(false);
        given(users.existsByEmail(anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> userSignService.signUp(userSignupRequest))
                .isInstanceOf(UserEmailDuplicationException.class);
    }

    @Test
    @DisplayName("회원가입 - 비밀번호 불일치 실패")
    void signup_password_not_correct() {
        // given
        UserRequest.SignUp signupRequest = new UserRequest.SignUp(
                USERNAME,
                EMAIL,
                PASSWORD,
                PASSWORD_CHECK_NOT_SAME
        );

        // when, then
        assertThatThrownBy(() -> userSignService.signUp(signupRequest))
                .isInstanceOf(UserNotCorrectPasswordException.class);
    }
}