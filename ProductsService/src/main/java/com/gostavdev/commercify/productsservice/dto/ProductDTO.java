package com.gostavdev.commercify.productsservice.dto;

public record ProductDTO(
        Long productId,
        String name,
        String description,
        String currency,
        Double unitPrice,
        Integer stock) {
}
