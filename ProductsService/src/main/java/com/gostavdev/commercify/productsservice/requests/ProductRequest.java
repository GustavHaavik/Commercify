package com.gostavdev.commercify.productsservice.requests;

public record ProductRequest(
        String name,
        String description,
        Double unitPrice,
        String currency,
        Integer stock) {
}
