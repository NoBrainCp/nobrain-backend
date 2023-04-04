package com.nobrain.bookmarking.domain.auth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.UnauthorizedException;

public class TokenInvalidException extends UnauthorizedException {

    public TokenInvalidException(String value) {
        super(value, ErrorCode.INVALID_TOKEN);
    }
}
