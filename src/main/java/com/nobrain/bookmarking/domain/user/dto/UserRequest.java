package com.nobrain.bookmarking.domain.user.dto;

import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.type.RoleType;
import lombok.*;

import javax.validation.constraints.*;
import java.util.Collections;

public class UserRequest {

    @Getter
    public static class SignIn {
        private String loginId;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {

        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @Size(min = 2, max = 8, message = "닉네임의 길이는 최소 2글자에서 최대 8글자까지 가능합니다.")
        private String name;

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Pattern(regexp="^(?=.*[\\d\\W])(?=.*[a-zA-Z]).{8,20}$",
                message = "비밀번호는 영문 소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 이상의 비밀번호여야 합니다.")
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeForgotPassword {
        private String username;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = "비밀번호는 숫자와 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
        private String password;
        private String passwordCheck;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePassword {
        private String prePassword;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = "비밀번호는 숫자와 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
        private String newPassword;
        private String passwordCheck;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeName {
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @Size(min = 2, max = 8, message = "닉네임의 길이는 최소 2글자에서 최대 8글자까지 가능합니다.")
        private String newName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveUser {
        private String password;
    }
}
