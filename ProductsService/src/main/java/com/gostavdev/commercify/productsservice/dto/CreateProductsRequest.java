package com.gostavdev.commercify.productsservice.dto;

import java.util.List;

public record CreateProductsRequest(List<ProductDTO> products) {
}
