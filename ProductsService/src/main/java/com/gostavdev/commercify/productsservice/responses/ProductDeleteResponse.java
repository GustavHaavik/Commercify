package com.gostavdev.commercify.productsservice.responses;

public record ProductDeleteResponse(boolean deleted, String message) {
    public ProductDeleteResponse(boolean deleted) {
        this(deleted, deleted ? "Product deleted successfully" : "Product deactivated successfully");
    }
}
