package com.gostavdev.commercify.orderservice;

import com.gostavdev.commercify.orderservice.dto.CreateOrderRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "orders")
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private OrderStatus status; // new, processing, completed, cancelled
    private LocalDateTime orderDate;

    public Order(CreateOrderRequest orderRequest) {
        this.userId = orderRequest.userId();
        this.productId = orderRequest.productId();
        this.quantity = orderRequest.quantity();
    }
}
