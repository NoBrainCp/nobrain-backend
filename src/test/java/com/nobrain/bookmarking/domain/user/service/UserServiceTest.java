package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class UserServiceTest extends ServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("프로필 조회")
    void getMyProfile() {
        User user = User.builder()
                .name(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        given(users.findById(1L)).willReturn(Optional.of(user));

        UserPayload payload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();

        UserResponse.Profile userProfileResponse = UserResponse.Profile.toDto(user);
        assertThat(userService.getMyProfile(payload)).isEqualTo(userProfileResponse);
    }

    @Test
    @Disabled
    void getUserInfo() {
    }

    @Test
    @DisplayName("유저 이름 변경")
    @Disabled
    void changeName() {
        User user = User.builder()
                .name(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        users.save(user);

        UserPayload payload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();

        UserRequest.ChangeName UsernameUpdateRequest = new UserRequest.ChangeName("change_name");
        userService.changeName(payload, UsernameUpdateRequest);

        assertThat(users.findByName("change_name").orElseThrow().getName()).isEqualTo("change_name");
    }

    @Test
    @Disabled
    void changeForgotPassword() {
    }

    @Test
    @Disabled
    void changePassword() {
    }

    @Test
    @Disabled
    void changeProfileImage() {
    }

    @Test
    @Disabled
    void delete() {
    }

    @Test
    @Disabled
    void deleteProfileImage() {
    }

    private UserPayload createPayload(Long userId, String username) {
        return UserPayload.builder()
                .userId(userId)
                .username(username)
                .build();
    }
}