package com.nobrain.bookmarking.domain.user.dto;

import com.nobrain.bookmarking.domain.user.dto.projection.UserInfo;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class UserResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SignIn {
        private Long userId;
        private String email;
        private String username;
        private String accessToken;
        private String tokenType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private Long userId;
        private String username;
        private String email;
        private String profileImage;

        public static Info toDto(UserInfo userInfo) {
            return UserResponse.Info.builder()
                    .userId(userInfo.getId())
                    .username(userInfo.getName())
                    .email(userInfo.getEmail())
                    .profileImage(userInfo.getProfileImage())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private Long userId;
        private String loginId;
        private String email;
        private String username;
        private String phoneNumber;
        private LocalDate birthDate;
        private String profileImage;
        private List<String> roles;

        public static Profile toDto(User user) {
            return Profile.builder()
                    .userId(user.getId())
                    .loginId(user.getLoginId())
                    .email(user.getEmail())
                    .username(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .birthDate(user.getBirthDate())
                    .profileImage(user.getProfileImage())
                    .roles(user.getRoles())
                    .build();
        }
    }
}
