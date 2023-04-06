package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;

public interface JwtTokenProvider {

    String generateToken(UserPayload payload);

    String generateAccessToken(String refreshToken);

    RefreshToken generateRefreshToken(Long userPk);

    void validateToken(String token);

    UserPayload getPayload(String token);
}
