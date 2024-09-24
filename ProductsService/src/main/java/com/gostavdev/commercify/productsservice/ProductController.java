package com.gostavdev.commercify.productsservice;

import com.gostavdev.commercify.productsservice.dto.CreateProductRequests;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        System.out.println("ProductController.getProductById() called with id: " + id);
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequests request) {
        Product product = new Product(request);
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
