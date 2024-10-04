package com.gostavdev.commercify.orderservice.dto.api;

public record PaymentResponse(
        Long paymentId,
        String status,
        String clientSecret) {

}
