package com.sparta.blogproj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CheckUserInformation.class)
    public ResponseEntity<Object> handleInvalidTokenException(CheckUserInformation e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TokenVerification.class)
    public ResponseEntity<Object> handleInvalidPostOwnerException(TokenVerification e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameCheckExistence.class)
    public ResponseEntity<Object> handleDuplicateUsernameException(UsernameCheckExistence e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserVerification.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserVerification e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
