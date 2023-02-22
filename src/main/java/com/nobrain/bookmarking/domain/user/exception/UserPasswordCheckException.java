package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UserPasswordCheckException extends InvalidValueException {
    public UserPasswordCheckException(String value) {
        super(value);
    }
}
