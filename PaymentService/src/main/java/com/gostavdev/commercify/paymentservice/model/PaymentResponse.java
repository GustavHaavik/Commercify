package com.gostavdev.commercify.paymentservice.model;

public record PaymentResponse(Long paymentId, PaymentStatus status, String clientSecret) {
    public PaymentResponse {
        if (paymentId == null || status == null || clientSecret == null) {
            throw new IllegalArgumentException("PaymentResponse fields cannot be null");
        }
    }

    public static PaymentResponse FailedPayment() {
        return new PaymentResponse(-1L, PaymentStatus.FAILED, "");
    }
}

