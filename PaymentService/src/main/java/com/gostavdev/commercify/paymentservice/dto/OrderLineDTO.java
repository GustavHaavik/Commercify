package com.gostavdev.commercify.paymentservice.dto;

public record OrderLineDTO(
        Long productId,
        String stripeProductId,
        Integer quantity,
        Double unitPrice) {
}
