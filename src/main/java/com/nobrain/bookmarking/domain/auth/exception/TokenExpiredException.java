package com.nobrain.bookmarking.domain.auth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {

    public TokenExpiredException(String value) {
        super(value, ErrorCode.TOKEN_EXPIRATION);
    }
}
