package com.startit.authservice.exception;

public class BadRegistrationDataException extends RuntimeException {
    public BadRegistrationDataException(String message) {
        super(message);
    }
}
