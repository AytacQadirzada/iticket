package com.example.iticket.exception;

import org.springframework.http.HttpStatus;

public class OtpException extends BaseException {
    public OtpException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
