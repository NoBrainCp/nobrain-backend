package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UserEmailNotFoundException extends InvalidValueException {

    public UserEmailNotFoundException(String email) {
        super(email, ErrorCode.EMAIL_NOT_FOUND);
    }
}
