package com.gostavdev.commercify.paymentservice.dto;

public record PaymentRequest(Long orderId, Double totalAmount, String currency, String paymentProvider) {
    public String successUrl() {
        return "http://localhost:3000/checkout/success";
    }
}