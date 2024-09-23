package com.gostavdev.commercify.orderservice.dto;


public record ProductDto(Long id, String name, Double price, Integer stock) {
    public ProductDto {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }
        if (stock == null) {
            throw new IllegalArgumentException("stock cannot be null");
        }
    }
}
