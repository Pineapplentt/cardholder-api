package com.jazztech.exception;

public class InvalidLimitException extends RuntimeException {
    public InvalidLimitException(String message) {
        super(message);
    }
}
