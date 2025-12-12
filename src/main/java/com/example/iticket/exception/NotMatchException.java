package com.example.iticket.exception;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.http.HttpStatus;

public class NotMatchException extends BaseException {
    public NotMatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
