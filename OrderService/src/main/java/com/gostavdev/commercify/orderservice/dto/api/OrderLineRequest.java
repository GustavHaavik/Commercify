package com.gostavdev.commercify.orderservice.dto.api;

public record OrderLineRequest(
        Long productId,
        Integer quantity) {
}
