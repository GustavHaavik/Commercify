package com.gostavdev.commercify.orderservice.dto;

public record ProductDto(
        String id,
        String stripeId,
        String name,
        String description,
        Double unitPrice,
        Integer stock) {
}
