package com.nobrain.bookmarking.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginRequest {

    private String loginId;
    private String password;

    @JsonProperty("isKeepLoggedIn")
    private boolean isKeepLoggedIn;
}
