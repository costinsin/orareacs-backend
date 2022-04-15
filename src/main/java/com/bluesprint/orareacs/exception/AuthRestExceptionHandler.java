package com.bluesprint.orareacs.exception;

import com.bluesprint.orareacs.dto.AuthErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(MissingRefreshTokenException exc) {

        AuthErrorResponse error = AuthErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<AuthErrorResponse> handleException(TokenValidationException exc) {

        AuthErrorResponse error = AuthErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
