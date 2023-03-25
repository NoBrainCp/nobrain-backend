package com.nobrain.bookmarking.infra.s3.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class FileNotConvertException extends InvalidValueException {

    public FileNotConvertException(String value) {
        super(value, ErrorCode.FILE_NOT_CONVERT);
    }
}
