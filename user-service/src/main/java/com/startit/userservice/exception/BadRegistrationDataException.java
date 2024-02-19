package com.startit.userservice.exception;

public class BadRegistrationDataException extends RuntimeException {
    public BadRegistrationDataException(String message) {
        super(message);
    }
}
