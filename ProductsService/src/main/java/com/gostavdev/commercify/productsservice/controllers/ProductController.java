package com.gostavdev.commercify.productsservice.controllers;

import com.gostavdev.commercify.productsservice.requests.CreateProductsRequest;
import com.gostavdev.commercify.productsservice.requests.ProductRequest;
import com.gostavdev.commercify.productsservice.services.ProductService;
import com.gostavdev.commercify.productsservice.dto.ProductDTO;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest request) {
        try {
            ProductDTO product = productService.saveProduct(request);
            return ResponseEntity.ok(product);
        } catch (StripeException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductDTO>> createBatchProducts(@RequestBody List<ProductRequest> request) {
        System.out.println("Products: " + request);
        try {
            List<ProductDTO> products = productService.saveProducts(request);
            return ResponseEntity.ok(products);
        } catch (StripeException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
