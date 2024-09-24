package com.gostavdev.commercify.paymentservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "payments")
@NoArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId; // The ID of the associated order
    private Double amount;
    @Column(name = "payment_method")
    private String paymentMethod; // e.g., Credit Card, PayPal
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // e.g., PENDING, COMPLETED, FAILED

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Payment(PaymentRequest paymentRequest, String paymentMethod) {
        this.orderId = paymentRequest.orderId();
        this.amount = paymentRequest.amount();
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
