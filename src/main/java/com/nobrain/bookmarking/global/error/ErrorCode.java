package com.nobrain.bookmarking.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),

    // User
    EMAIL_DUPLICATION(400, "U001", "Email is Duplication"),
    LOGIN_ID_DUPLICATION(400, "U002", "Login id is Duplication"),
    LOGIN_ID_NOT_FOUND(400, "U003", "Login id is not found"),
    INVALID_PASSWORD(400, "U004", "Password is not correct"),
    PHONE_NUMBER_DUPLICATION(400, "U005", "Phone number is Duplication"),

    // Category
    CATEGORY_NAME_DUPLICATION(400, "C001", "Category name is Duplication");

    private int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
