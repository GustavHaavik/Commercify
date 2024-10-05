package com.gostavdev.commercify.paymentservice.dto;

public record PaymentRequest(Long orderId, Double amount, String currency, String paymentProvider) {
}