package com.nobrain.bookmarking.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // General
    INVALID_INPUT_VALUE(400, "G001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "G002", "Not Allowed HTTP Method"),
    INTERNAL_SERVER_ERROR(500, "G003", "Server Error"),
    HANDLE_ACCESS_DENIED(403, "G004", "Access is Denied"),

    // User
    EMAIL_DUPLICATION(400, "U001", "Email is Duplication"),
    LOGIN_ID_DUPLICATION(400, "U002", "Login id is Duplication"),
    LOGIN_ID_NOT_FOUND(400, "U003", "Login id is not Found"),
    USERNAME_NOT_FOUND(400, "U004", "Username is not Found"),
    INVALID_PASSWORD(400, "U005", "Password is not Correct"),
    PHONE_NUMBER_DUPLICATION(400, "U006", "Phone number is Duplication"),

    // Category
    CATEGORY_NAME_DUPLICATION(400, "C001", "Category name is Duplication"),
    CATEGORY_NAME_NOT_FOUND(400, "C002", "Category name is not Found");

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
