package com.nobrain.bookmarking.domain.auth.exception;

import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class InvalidRefreshTokenException extends InvalidValueException {

    public InvalidRefreshTokenException(String value) {
        super(value);
    }
}
