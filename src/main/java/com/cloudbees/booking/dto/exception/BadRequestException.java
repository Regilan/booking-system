package com.cloudbees.booking.dto.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }

}
