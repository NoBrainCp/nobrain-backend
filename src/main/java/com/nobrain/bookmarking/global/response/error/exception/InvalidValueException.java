package com.nobrain.bookmarking.global.response.error.exception;

import com.nobrain.bookmarking.global.response.error.ErrorCode;

public class InvalidValueException extends ApplicationException {

    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
