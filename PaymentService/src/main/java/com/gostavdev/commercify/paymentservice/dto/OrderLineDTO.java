package com.gostavdev.commercify.paymentservice.dto;

public record OrderLineDTO(
        Long productId,
        Integer quantity,
        Double unitPrice) {
}
