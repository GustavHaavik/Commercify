package com.gostavdev.commercify.orderservice.dto;

public record ProductDto(
        String id,
        String name,
        String description,
        Double unitPrice,
        Integer stock) {
}
