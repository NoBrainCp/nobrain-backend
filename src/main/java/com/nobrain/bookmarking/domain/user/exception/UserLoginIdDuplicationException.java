package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UserLoginIdDuplicationException extends InvalidValueException {

    public UserLoginIdDuplicationException(String loginId) {
        super(loginId, ErrorCode.LOGIN_ID_DUPLICATION);
    }
}
