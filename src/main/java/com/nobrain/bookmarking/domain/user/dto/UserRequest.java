package com.nobrain.bookmarking.domain.user.dto;

import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.global.type.RoleType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;

public class UserRequest {

    @Getter
    public static class SignIn {
        private String loginId;
        private String password;
    }

    @Getter
    public static class SignUp {
        private String loginId;
        private String email;
        private String password;
        private String passwordCheck;
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;

        public void encodePassword(String encodedPassword) {
            this.password = encodedPassword;
        }

        public User toEntity() {
            return User.builder()
                    .loginId(this.loginId)
                    .email(this.email)
                    .password(this.password)
                    .name(this.name)
                    .phoneNumber(this.phoneNumber)
                    .birthDate(this.birthDate)
                    .roles(Collections.singletonList(RoleType.USER.getKey()))
                    .build();
        }
    }

    @Getter
    public static class FindLoginIdByPhoneNumber {
        private String name;
        private String phoneNumber;
    }

    @Getter
    public static class FindLoginIdByEmail {
        private String name;
        private String email;
    }

    @Getter
    public static class ChangeUserName {
        private String username;
    }

    @Getter
    public static class ChangePassword {
        private String loginId;
        private String password;
        private String passwordCheck;
    }
}
