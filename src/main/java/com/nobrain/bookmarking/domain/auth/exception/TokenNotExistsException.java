package com.nobrain.bookmarking.domain.auth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.UnauthorizedException;

public class TokenNotExistsException extends UnauthorizedException {

    public TokenNotExistsException() {
        super("존재하지 않는 토큰입니다.", ErrorCode.TOKEN_NOT_EXISTS);
    }
}
