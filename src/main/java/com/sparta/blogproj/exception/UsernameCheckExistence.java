package com.sparta.blogproj.exception;

public class UsernameCheckExistence extends RuntimeException {
    public UsernameCheckExistence(String message) {
        super(message);
    }
}
