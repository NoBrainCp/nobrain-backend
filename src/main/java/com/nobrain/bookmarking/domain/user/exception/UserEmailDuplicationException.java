package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.response.error.ErrorCode;
import com.nobrain.bookmarking.global.response.error.exception.InvalidValueException;

public class UserEmailDuplicationException extends InvalidValueException {

    public UserEmailDuplicationException(String email) {
        super(email, ErrorCode.EMAIL_DUPLICATION);
    }
}
