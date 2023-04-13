package com.nobrain.bookmarking.domain.user.dto;

import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.type.RoleType;
import lombok.Getter;

import java.util.Collections;

public class UserRequest {

    @Getter
    public static class SignIn {
        private String loginId;
        private String password;
    }

    @Getter
    public static class SignUp {
        private String name;
        private String email;
        private String password;
        private String passwordCheck;

        public void encodePassword(String encodedPassword) {
            this.password = encodedPassword;
        }

        public User toEntity() {
            return User.builder()
                    .name(this.name)
                    .email(this.email)
                    .password(this.password)
                    .roles(Collections.singletonList(RoleType.USER.getRole()))
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
    public static class ChangeForgotPassword {
        private String username;
        private String password;
        private String passwordCheck;
    }

    @Getter
    public static class ChangePassword {
        private String prePassword;
        private String newPassword;
        private String passwordCheck;
    }

    @Getter
    public static class ChangeName {
        private String newName;
    }

    @Getter
    public static class RemoveUser {
        private String password;
    }
}
