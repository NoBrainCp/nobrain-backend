package com.nobrain.bookmarking.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nobrain.bookmarking.domain.user.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignupRequest {
    String loginId;
    String email;
    String password;
    String name;
    String phoneNumber;
    LocalDate birthDate;

    public User encodePasswordToEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .loginId(this.loginId)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .birthDate(this.birthDate)
                .build();
    }
}
