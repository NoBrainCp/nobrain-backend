package com.nobrain.bookmarking.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.global.type.RoleType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignUpRequest {
    String loginId;
    String email;
    String password;
    String name;
    String phoneNumber;
    LocalDate birthDate;

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
