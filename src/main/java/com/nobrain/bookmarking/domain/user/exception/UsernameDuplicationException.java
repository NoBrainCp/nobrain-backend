package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UsernameDuplicationException extends InvalidValueException {

    public UsernameDuplicationException(String username) {
        super(username);
    }
}
