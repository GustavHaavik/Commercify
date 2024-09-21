package com.gostavdev.commercify.productsservice.dto;

public record CreateProductRequests(String name, String description, double price, int stock) {
    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public double price() {
        return price;
    }

    @Override
    public int stock() {
        return stock;
    }
}
