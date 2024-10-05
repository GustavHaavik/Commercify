package com.gostavdev.commercify.paymentservice.entities;

import com.gostavdev.commercify.paymentservice.dto.PaymentRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "payments")
@NoArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;
    private Double amount;
    @Column(name = "payment_method")
    private String paymentMethod; // e.g., Credit Card, PayPal
    private String paymentProvider;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Payment(PaymentRequest paymentRequest) {
        this.orderId = paymentRequest.orderId();
        this.amount = paymentRequest.amount();
        this.paymentProvider = paymentRequest.paymentProvider();
        this.status = PaymentStatus.PENDING;
    }
}
