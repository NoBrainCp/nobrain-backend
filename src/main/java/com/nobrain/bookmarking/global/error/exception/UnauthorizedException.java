package com.nobrain.bookmarking.global.error.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;

public class UnauthorizedException extends ApplicationException {

    public UnauthorizedException(String value) {
        super(value, ErrorCode.UNAUTHORIZATION);
    }

    public UnauthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
