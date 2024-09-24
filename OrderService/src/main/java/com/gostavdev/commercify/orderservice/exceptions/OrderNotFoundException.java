package com.gostavdev.commercify.orderservice.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(String.format("Order not found: %s", message));
    }
}
