package com.gostavdev.commercify.productsservice;


import com.gostavdev.commercify.productsservice.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "products")
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double unitPrice;
    private Integer stock;

    protected Product(ProductDTO request) {
        this.name = request.name();
        this.description = request.description();
        this.unitPrice = request.unitPrice();
        this.stock = request.stock();
    }
}
