package com.nobrain.bookmarking.domain.auth.dto;

import lombok.Getter;

@Getter
public class AccessTokenRequest {

    private String refreshToken;
}
