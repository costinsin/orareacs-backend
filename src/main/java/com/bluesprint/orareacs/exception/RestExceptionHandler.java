package com.bluesprint.orareacs.exception;

import com.bluesprint.orareacs.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MissingRefreshTokenException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(TokenValidationException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(EmailAlreadyExistsException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UsernameAlreadyExistsException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(InvalidCodeException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(TimetableAlreadyExistsException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(TimetableMissingException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UsernameNotFoundException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserAccessNotPermittedException exc) {

        ErrorResponse error = ErrorResponse.builder()
                .message(exc.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .timeStamp(System.currentTimeMillis() / 1000)
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
