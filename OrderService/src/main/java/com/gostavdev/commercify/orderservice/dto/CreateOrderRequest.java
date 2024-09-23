package com.gostavdev.commercify.orderservice.dto;

public record CreateOrderRequest(Long productId, Long userId, Integer quantity) {
    public CreateOrderRequest {
        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("quantity cannot be null");
        }
    }
}
