package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.dto.projection.UserInfo;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.exception.UsernameDuplicationException;
import com.nobrain.bookmarking.global.security.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest extends ServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncryptor encryptor;

    private User user;
    private UserPayload payload;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(USER_ID)
                .name(USERNAME)
                .email(EMAIL)
                .password(encryptor.encrypt(PASSWORD))
                .profileImage(PROFILE_IMG)
                .build();

        payload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();
    }

    @Test
    @DisplayName("프로필 조회 - 성공")
    void getMyProfile() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        // when
        UserResponse.Profile actual = userService.getMyProfile(payload);

        // then
        UserResponse.Profile expected = UserResponse.Profile.toDto(user);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("유저 정보 조회 - 성공")
    void getUserInfo() {
        // given
        UserInfo userInfo = Mockito.mock(UserInfo.class);
        given(users.findUserInfoByName(anyString()))
                .willReturn(Optional.of(userInfo));

        given(userInfo.getId()).willReturn(user.getId());
        given(userInfo.getName()).willReturn(user.getName());
        given(userInfo.getEmail()).willReturn(user.getEmail());
        given(userInfo.getProfileImage()).willReturn(user.getProfileImage());

        // when
        UserResponse.Info actual = userService.getUserInfo(user.getName());

        // then
        UserResponse.Info expected = UserResponse.Info.toDto(userInfo);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
        verify(users).findUserInfoByName(user.getName());
    }

    @Test
    @DisplayName("유저 정보 조회 - 존재하지 않는 유저 실패")
    void getUserInfo_user_not_found() {
        // given
        given(users.findUserInfoByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userService.getUserInfo(user.getName()))
                .isInstanceOf(UserNotFoundException.class);
        verify(users).findUserInfoByName(user.getName());
    }

    @Test
    @DisplayName("유저 이름 변경")
    void changeName() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        UserRequest.ChangeName usernameUpdateRequest = new UserRequest.ChangeName(NEW_USERNAME);

        // when
        userService.changeName(payload, usernameUpdateRequest);

        // then
        assertThat(user.getName()).isEqualTo(NEW_USERNAME);
        verify(users).findById(anyLong());
    }

    @Test
    @DisplayName("유저 이름 변경 - 유저 이름 중복 실패")
    void changeName_duplication_name() {
        // given
        given(users.existsByName(anyString()))
                .willReturn(true);

        UserRequest.ChangeName usernameUpdateRequest = new UserRequest.ChangeName(NEW_USERNAME);

        // when, then
        assertThatThrownBy(() -> userService.changeName(payload, usernameUpdateRequest))
                .isInstanceOf(UsernameDuplicationException.class);
        verify(users).existsByName(NEW_USERNAME);
    }

    @Test
    @DisplayName("잊어버린 비밀번호 변경 - 성공")
    void changeForgotPassword() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));

        UserRequest.ChangeForgotPassword changeForgotPasswordRequest = new UserRequest.ChangeForgotPassword(
                user.getName(),
                CHANGE_PASSWORD,
                CHANGE_PASSWORD
        );

        // when
        userService.changeForgotPassword(changeForgotPasswordRequest);

        // then
        assertThat(encryptor.isMatch(CHANGE_PASSWORD, user.getPassword())).isTrue();
        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("잊어버린 비밀번호 변경 - 비밀번호와 비밀번호 확인이 다른 경우 실패")
    void changeForgotPassword_not_same_password() {
        // given
        UserRequest.ChangeForgotPassword changeForgotPasswordRequest = new UserRequest.ChangeForgotPassword(
                user.getName(),
                CHANGE_PASSWORD,
                CHANGE_PASSWORD_NOT_SAME
        );

        // when, then
        assertThatThrownBy(() -> userService.changeForgotPassword(changeForgotPasswordRequest))
                .isInstanceOf(UserNotCorrectPasswordException.class);
    }

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void changePassword() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        UserRequest.ChangePassword changePasswordRequest = new UserRequest.ChangePassword(
                PASSWORD,
                CHANGE_PASSWORD,
                CHANGE_PASSWORD
        );

        // when
        userService.changePassword(payload, changePasswordRequest);

        // then
        assertThat(encryptor.isMatch(CHANGE_PASSWORD, user.getPassword())).isTrue();
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("비밀번호 변경 - 변경할 비밀번호와 비밀번호 확인이 다른 경우 실패")
    void changePassword_not_same_password() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        UserRequest.ChangePassword changePasswordRequest = new UserRequest.ChangePassword(
                PASSWORD,
                CHANGE_PASSWORD,
                CHANGE_PASSWORD_NOT_SAME
        );

        // when, then
        assertThatThrownBy(() -> userService.changePassword(payload, changePasswordRequest))
                .isInstanceOf(UserNotCorrectPasswordException.class);
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("비밀번호 변경 - 변경전 비밀번호와 입력한 비밀번호가 다른 경우 실패")
    void changePassword_not_same_pre_password() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        UserRequest.ChangePassword changePasswordRequest = new UserRequest.ChangePassword(
                PASSWORD_NOT_SAME,
                CHANGE_PASSWORD,
                CHANGE_PASSWORD
        );

        // when, then
        assertThatThrownBy(() -> userService.changePassword(payload, changePasswordRequest))
                .isInstanceOf(UserNotCorrectPasswordException.class);
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("프로필 이미지 변경 - 성공")
    void changeProfileImage() throws IOException {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        MockMultipartFile changeProfileImg = new MockMultipartFile(
                CHANGE_PROFILE_IMG_NAME,
                CHANGE_PROFILE_IMG_PATH,
                PROFILE_IMG_CONTENT_TYPE_PNG,
                CHANGE_PROFILE_IMG_NAME.getBytes()
        );

        // when
        String actual = userService.changeProfileImage(payload, changeProfileImg);

        // then
        assertThat(actual).contains(CHANGE_PROFILE_IMG_PATH);
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("회원 탈퇴 - 성공")
    void delete() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        UserRequest.RemoveUser removeUserRequest = new UserRequest.RemoveUser(PASSWORD);

        // when, then
        assertThatCode(() -> userService.delete(payload, removeUserRequest))
                .doesNotThrowAnyException();
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("회원 탈퇴 - 비밀번호 불일치 실패")
    void delete_not_same_password() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        UserRequest.RemoveUser removeUserRequest = new UserRequest.RemoveUser(PASSWORD_NOT_SAME);

        // when, then
        assertThatThrownBy(() -> userService.delete(payload, removeUserRequest))
                .isInstanceOf(UserNotCorrectPasswordException.class);
        verify(users).findById(user.getId());
    }

    @Test
    @DisplayName("유저 이미지 삭제 - 성공")
    void deleteProfileImage() {
        // given
        given(users.findById(anyLong()))
                .willReturn(Optional.of(user));

        // when
        userService.deleteProfileImage(payload);

        // then
        assertThat(user.getProfileImage()).isNull();
        verify(users).findById(user.getId());
    }
}