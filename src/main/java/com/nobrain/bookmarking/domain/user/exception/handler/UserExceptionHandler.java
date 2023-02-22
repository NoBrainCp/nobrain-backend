package com.nobrain.bookmarking.domain.user.exception.handler;

import com.nobrain.bookmarking.domain.user.exception.UserEmailDuplicationException;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
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

    @ExceptionHandler(UserEmailDuplicationException.class)
    protected ResponseEntity<ErrorResponse> handleUserEmailDuplicationException(UserEmailDuplicationException e) {
        log.error("handleUserEmailDuplicationException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.EMAIL_DUPLICATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotCorrectPasswordException.class)
    protected ResponseEntity<ErrorResponse> handleUserPasswordCheckException(UserNotCorrectPasswordException e) {
        log.error("handleUserNotCorrectPasswordException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_PASSWORD);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
