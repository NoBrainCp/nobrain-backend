package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {

    String generateToken(UserPayload payload);

    String generateAccessToken(String refreshToken);

    RefreshToken generateRefreshToken(Long userPk);

    boolean validateToken(HttpServletRequest request);

    UserPayload getPayload(String token);
}
