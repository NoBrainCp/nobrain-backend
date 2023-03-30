package com.nobrain.bookmarking.global.oauth.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class NotFoundSocialLoginType extends InvalidValueException {

    public NotFoundSocialLoginType(String value) {
        super(value, ErrorCode.SOCIAL_LOGIN_TYPE_NOT_FOUND);
    }
}
