package com.gostavdev.commercify.productsservice.services;

import com.gostavdev.commercify.productsservice.dto.ProductDTO;
import com.gostavdev.commercify.productsservice.entities.Product;
import com.gostavdev.commercify.productsservice.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public Product saveProduct(ProductDTO product) {
        Product productEntity = new Product(product);

        return productRepository.save(productEntity);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
