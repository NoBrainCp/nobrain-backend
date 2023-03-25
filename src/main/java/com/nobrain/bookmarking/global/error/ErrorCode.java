package com.nobrain.bookmarking.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // General
    INVALID_INPUT_VALUE(400, "G001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "G002", "Not Allowed HTTP Method"),
    INTERNAL_SERVER_ERROR(500, "G003", "Server Error"),
    HANDLE_ACCESS_DENIED(403, "G004", "Access is Denied"),
    URL_NOT_FOUND(NOT_FOUND.value(), "G005", "등록하신 URL은 존재하지 않는 URL입니다."),
    FILE_NOT_CONVERT(NOT_ACCEPTABLE.value(), "G006", "등록할 수 없는 파일입니다."),

    // User
    USER_NOT_FOUND(400, "U001", "존재하지 않는 유저입니다."),
    EMAIL_NOT_FOUND(400, "U002", "등록되지 않은 이메일입니다."),
    EMAIL_DUPLICATION(400, "U003", "중복된 이메일입니다."),
    LOGIN_ID_DUPLICATION(400, "U004", "중복된 아이디입니다."),
    LOGIN_ID_NOT_FOUND(400, "U005", "아이디를 찾을 수 없습니다."),
    USERNAME_NOT_FOUND(400, "U006", "닉네임을 찾을 수 없습니다."),
    USERNAME_DUPLICATION(400, "U007", "중복된 닉네임 입니다."),
    INVALID_PASSWORD(400, "U008", "비밀번호가 일치하지 않습니다."),
    PHONE_NUMBER_DUPLICATION(400, "U009", "중복된 휴대폰 번호입니다."),

    // Category
    CATEGORY_NAME_DUPLICATION(400, "C001", "Category name is Duplication"),
    CATEGORY_NAME_NOT_FOUND(400, "C002", "Category name is not Found"),

    INVALID_AUTH_CODE(401, "A001", "인증 코드가 올바르지 않습니다.");

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
