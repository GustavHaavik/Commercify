package com.gostavdev.commercify.productsservice.controllers;

import com.gostavdev.commercify.productsservice.requests.ProductRequest;
import com.gostavdev.commercify.productsservice.responses.ProductDeleteResponse;
import com.gostavdev.commercify.productsservice.services.ProductService;
import com.gostavdev.commercify.productsservice.dto.ProductDTO;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest request) {
        ProductDTO product = productService.saveProduct(request);
        return ResponseEntity.ok(product);
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
        List<ProductDTO> products = productService.saveProducts(request);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = productService.deleteProduct(id, false);
            return ResponseEntity.ok(new ProductDeleteResponse(deleted));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ProductDeleteResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<ProductDeleteResponse> forceDeleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = productService.deleteProduct(id, true);
            return ResponseEntity.ok(new ProductDeleteResponse(deleted));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ProductDeleteResponse(false, e.getMessage()));
        }
    }
}
