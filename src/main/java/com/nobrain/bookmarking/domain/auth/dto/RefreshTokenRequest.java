package com.nobrain.bookmarking.domain.auth.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RefreshTokenRequest {

    private String loginId;
    private String password;
    private List<String> roles;
}
