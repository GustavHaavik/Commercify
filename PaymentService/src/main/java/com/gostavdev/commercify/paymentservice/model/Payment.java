package com.gostavdev.commercify.paymentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long orderId; // The ID of the associated order
    private Double amount;
    private String paymentMethod; // e.g., Credit Card, PayPal
    @Setter
    private PaymentStatus status; // e.g., PENDING, COMPLETED, FAILED

    private LocalDateTime paymentDate;

    public Payment(PaymentRequest paymentRequest) {
        this.orderId = paymentRequest.orderId();
        this.amount = paymentRequest.amount();
    }
}
