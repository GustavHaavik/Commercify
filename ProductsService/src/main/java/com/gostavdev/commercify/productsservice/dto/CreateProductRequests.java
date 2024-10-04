package com.gostavdev.commercify.productsservice.dto;

public record CreateProductRequests(
        String name,
        String description,
        Double unitPrice,
        Integer stock) {
}
