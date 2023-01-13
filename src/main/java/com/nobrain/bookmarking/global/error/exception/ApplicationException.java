package com.nobrain.bookmarking.global.error.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;

public class ApplicationException extends RuntimeException {
    private ErrorCode errorCode;

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
