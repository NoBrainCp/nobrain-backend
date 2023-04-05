package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {

    String generateToken(UserPayload payload);

    String generateAccessToken(String refreshToken);

    RefreshToken generateRefreshToken(Long userPk);

    String resolveToken(HttpServletRequest request);

    boolean validateToken(String token);

    UserPayload getPayload(HttpServletRequest request);
}
