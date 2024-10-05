package com.gostavdev.commercify.productsservice.entities;


import com.gostavdev.commercify.productsservice.requests.ProductRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "products")
@NoArgsConstructor
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String currency;
    private Double unitPrice;
    private Integer stock;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ProductEntity(ProductRequest request) {
        this.name = request.name();
        this.description = request.description();
        this.unitPrice = request.unitPrice();
        this.stock = request.stock();
        this.currency = request.currency();
    }
}
