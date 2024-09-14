package com.gostavdev.commercify.paymentservice.exceptions;

public class InvalidEventDataException extends RuntimeException {

    public InvalidEventDataException(String message) {
        super(message);
    }
}