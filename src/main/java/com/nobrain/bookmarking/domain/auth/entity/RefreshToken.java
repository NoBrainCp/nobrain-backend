package com.nobrain.bookmarking.domain.auth.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 12)
public class RefreshToken {

    @Id
    private final String refreshToken;
    private final Long userId;

    public RefreshToken(final String refreshToken, final Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
