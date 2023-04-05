package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UserNotFoundException extends InvalidValueException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String value) {
        super(value, ErrorCode.USER_NOT_FOUND);
    }
}
