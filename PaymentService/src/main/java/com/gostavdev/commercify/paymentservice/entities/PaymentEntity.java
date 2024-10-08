package com.gostavdev.commercify.paymentservice.entities;

import com.gostavdev.commercify.paymentservice.PaymentProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String stripePaymentIntent;

    @Column(name = "order_id", nullable = false)
    private Long orderId;
    private Double totalAmount;
    @Column(name = "payment_method")
    private String paymentMethod; // e.g., Credit Card, PayPal
    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
