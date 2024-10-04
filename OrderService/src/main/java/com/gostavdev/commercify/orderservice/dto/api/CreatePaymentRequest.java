package com.gostavdev.commercify.orderservice.dto.api;

public record CreatePaymentRequest(
        Long orderId,
        Double amount,
        String currency) {
}
