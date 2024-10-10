package com.gostavdev.commercify.paymentservice.controllers;

import com.gostavdev.commercify.paymentservice.PaymentProvider;
import com.gostavdev.commercify.paymentservice.dto.CancelPaymentResponse;
import com.gostavdev.commercify.paymentservice.dto.PaymentRequest;
import com.gostavdev.commercify.paymentservice.dto.PaymentResponse;
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

    @GetMapping("/cancel/{orderId}")
    public ResponseEntity<CancelPaymentResponse> cancelPayment(@PathVariable Long orderId) {
        CancelPaymentResponse response = paymentService.cancelPayment(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pay/{paymentProvider}")
    public ResponseEntity<PaymentResponse> makePayment(@PathVariable String paymentProvider, @RequestBody PaymentRequest paymentRequest) {
        PaymentProvider provider = PaymentProvider.valueOf(paymentProvider.toUpperCase());
        PaymentResponse response = paymentService.makePayment(provider, paymentRequest);
        return ResponseEntity.ok(response);
    }
}