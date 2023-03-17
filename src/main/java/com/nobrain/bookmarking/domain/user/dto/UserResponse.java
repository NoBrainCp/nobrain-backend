package com.nobrain.bookmarking.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {
        private Long userId;
        private String username;
        private String email;
        private String profileImage;
    }

    @Getter
    @Builder
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
    }
}
