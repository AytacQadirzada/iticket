package com.example.iticket.exception;

import org.springframework.http.HttpStatus;

public class RegistrationException extends BaseException {
    public RegistrationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
