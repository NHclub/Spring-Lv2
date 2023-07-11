package com.sparta.blogproj.exception;

public class UserVerification extends RuntimeException{
    public UserVerification(String message) {
        super(message);
    }
}
