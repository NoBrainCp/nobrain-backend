package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UsernameNotFoundException extends InvalidValueException {

    public UsernameNotFoundException(String username) {
        super(username, ErrorCode.USERNAME_NOT_FOUND);
    }
}
