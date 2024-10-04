package com.gostavdev.commercify.orderservice.dto;


public record OrderLineDTO(
        Long productId,
        Integer quantity,
        Double unitPrice) {
}
