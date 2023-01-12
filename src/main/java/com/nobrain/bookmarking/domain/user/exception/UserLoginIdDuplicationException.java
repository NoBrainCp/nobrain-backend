package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.response.error.ErrorCode;
import com.nobrain.bookmarking.global.response.error.exception.InvalidValueException;

public class UserLoginIdDuplicationException extends InvalidValueException {

    public UserLoginIdDuplicationException(String loginId) {
        super(loginId, ErrorCode.LOGIN_ID_DUPLICATION);
    }
}
