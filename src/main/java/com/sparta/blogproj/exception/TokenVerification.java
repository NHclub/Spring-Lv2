package com.sparta.blogproj.exception;

public class TokenVerification extends RuntimeException {
    public TokenVerification(String message) {
        super(message);
    }
}
