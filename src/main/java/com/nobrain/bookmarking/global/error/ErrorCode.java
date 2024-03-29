package com.nobrain.bookmarking.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import static org.springframework.http.HttpStatus.*;

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

    // Bookmark
    BOOKMARK_DUPLICATION(PRECONDITION_FAILED.value(), "B001", "카테고리에 이미 같은 북마크가 존재합니다."),

    // Auth
    UNAUTHORIZATION(UNAUTHORIZED.value(), "A001", "접근 권한이 없습니다."),
    INVALID_AUTH_CODE(UNAUTHORIZED.value(), "A002", "인증 코드가 올바르지 않습니다."),
    INVALID_TOKEN(UNAUTHORIZED.value(), "A003", "잘못된 형식의 토큰입니다."),
    TOKEN_NOT_EXISTS(UNAUTHORIZED.value(), "A004", "토큰이 존재하지 않습니다"),
    TOKEN_EXPIRATION(UNAUTHORIZED.value(), "A005", "토큰의 유효시간이 만료되었습니다."),
    INVALID_AUTHORIZATION_HEADER(UNAUTHORIZED.value(), "A006", "유효하지 않은 인증 헤더입니다."),

    // OAuth
    INVALID_OAUTH_PROVIDER(UNAUTHORIZED.value(), "O001", "지원되지 않은 OAuth 유형입니다.");

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
