package com.gostavdev.commercify.paymentservice.dto;

public record PaymentRequest(Long orderId, String currency, String paymentProvider) {
    public String successUrl() {
        return "http://localhost:3000/checkout/success";
    }

    public String cancelUrl() {
        return "http://localhost:3000/checkout/cancel";
    }
}