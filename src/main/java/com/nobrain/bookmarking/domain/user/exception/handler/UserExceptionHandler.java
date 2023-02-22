package com.nobrain.bookmarking.domain.user.exception.handler;

import com.nobrain.bookmarking.domain.user.exception.UserPasswordCheckException;
import com.nobrain.bookmarking.global.error.ErrorCode;
import com.nobrain.bookmarking.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserPasswordCheckException.class)
    protected ResponseEntity<ErrorResponse> handleUserPasswordCheckException(UserPasswordCheckException e) {
        log.error("handleUserPasswordCheckException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.PASSWORD_CHECK);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
