package com.nobrain.bookmarking.domain.user.exception;

import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.exception.InvalidValueException;

public class UserPhoneNumberDuplicationException extends InvalidValueException {

    public UserPhoneNumberDuplicationException(String phoneNumber) {
        super(phoneNumber, ErrorCode.PHONE_NUMBER_DUPLICATION);
    }
}
