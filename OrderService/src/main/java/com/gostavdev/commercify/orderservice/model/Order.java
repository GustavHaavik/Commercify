package com.gostavdev.commercify.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public Order(Long userId) {
        this.userId = userId;
        this.status = OrderStatus.PENDING;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedDate = LocalDateTime.now();
    }

    public double calculateTotalPrice() {
        double total = 0.0;
        for (OrderLine orderLine : orderLines) {
            total += orderLine.getQuantity() * orderLine.getProduct().price();
        }
        return total;
    }
}
