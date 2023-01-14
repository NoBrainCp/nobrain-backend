package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UserNotCorrectPasswordException extends InvalidValueException {

    public UserNotCorrectPasswordException(String password) {
        super(password, ErrorCode.INVALID_PASSWORD);
    }
}
