package com.nobrain.bookmarking.domain.auth.dto;

import lombok.Getter;

@Getter
public class LogoutRequest {

    private String refreshToken;
}
