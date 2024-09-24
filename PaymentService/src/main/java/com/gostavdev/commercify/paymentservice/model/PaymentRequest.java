package com.gostavdev.commercify.paymentservice.model;

public record PaymentRequest(Long orderId, Double amount, String currency, String paymentMethod) {
    public PaymentRequest {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("currency cannot be null");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("paymentMethod cannot be null");
        }
    }
}