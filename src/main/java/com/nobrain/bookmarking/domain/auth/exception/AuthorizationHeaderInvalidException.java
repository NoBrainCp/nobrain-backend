package com.nobrain.bookmarking.domain.auth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.UnauthorizedException;

public class AuthorizationHeaderInvalidException extends UnauthorizedException {

    public AuthorizationHeaderInvalidException() {
        super(ErrorCode.INVALID_AUTHORIZATION_HEADER);
    }
}
