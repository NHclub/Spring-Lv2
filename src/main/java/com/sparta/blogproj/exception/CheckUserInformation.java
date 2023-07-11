package com.sparta.blogproj.exception;

public class CheckUserInformation extends RuntimeException {
    public CheckUserInformation(String message) {
        super(message);
    }
}
