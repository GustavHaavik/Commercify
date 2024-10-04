package com.gostavdev.commercify.productsservice.dto;

public record ProductDTO(
        String name,
        String description,
        Double unitPrice,
        Integer stock) {
}
