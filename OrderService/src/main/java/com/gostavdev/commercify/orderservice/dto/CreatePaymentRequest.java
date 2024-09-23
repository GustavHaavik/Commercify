package com.gostavdev.commercify.orderservice.dto;

public record CreatePaymentRequest(Long orderId, Double amount, String currency) {
    public CreatePaymentRequest {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("currency cannot be null");
        }
    }
}
