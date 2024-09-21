package com.gostavdev.commercify.productsservice;


import com.gostavdev.commercify.productsservice.dto.CreateProductRequests;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "products")
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;

    protected Product(CreateProductRequests request) {
        this.name = request.name();
        this.description = request.description();
        this.price = request.price();
        this.stock = request.stock();
    }
}
