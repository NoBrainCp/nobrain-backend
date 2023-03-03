package com.nobrain.bookmarking.domain.util.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UrlNotFoundException extends InvalidValueException {

    public UrlNotFoundException(String value) {
        super(value, ErrorCode.URL_NOT_FOUND);
    }
}
