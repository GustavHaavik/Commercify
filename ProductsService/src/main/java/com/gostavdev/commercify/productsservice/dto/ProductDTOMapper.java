package com.gostavdev.commercify.productsservice.dto;

import com.gostavdev.commercify.productsservice.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductDTOMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStock()
        );
    }
}
