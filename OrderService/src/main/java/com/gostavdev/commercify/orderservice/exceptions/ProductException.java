package com.gostavdev.commercify.orderservice.exceptions;

public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(String.format("Product Exception: %s", message));
    }
}
