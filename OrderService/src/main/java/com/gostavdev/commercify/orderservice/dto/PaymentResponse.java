package com.gostavdev.commercify.orderservice.dto;

public record PaymentResponse(Long paymentId, String status, String clientSecret) {
    public PaymentResponse {
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        if (clientSecret == null) {
            throw new IllegalArgumentException("clientSecret cannot be null");
        }
    }
}
