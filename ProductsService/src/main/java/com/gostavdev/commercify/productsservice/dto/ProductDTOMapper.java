package com.gostavdev.commercify.productsservice.dto;

import com.gostavdev.commercify.productsservice.entities.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductDTOMapper implements Function<ProductEntity, ProductDTO> {
    @Override
    public ProductDTO apply(ProductEntity product) {
        return new ProductDTO(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getCurrency(),
                product.getUnitPrice(),
                product.getStock()
        );
    }
}
