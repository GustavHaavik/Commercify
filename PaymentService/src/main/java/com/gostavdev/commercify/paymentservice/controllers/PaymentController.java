package com.gostavdev.commercify.paymentservice.controllers;

import com.gostavdev.commercify.paymentservice.services.PaymentService;
import com.gostavdev.commercify.paymentservice.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getPaymentStatus(@PathVariable Long orderId) {
        PaymentStatus status = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(status.name());
    }
}