package com.gostavdev.commercify.paymentservice.controllers;

import com.gostavdev.commercify.paymentservice.dto.PaymentRequest;
import com.gostavdev.commercify.paymentservice.dto.PaymentResponse;
import com.gostavdev.commercify.paymentservice.services.StripeService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripe")
@AllArgsConstructor
public class StripeController {
    private final StripeService stripeService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<PaymentResponse> createCheckoutSession(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = stripeService.checkoutSession(paymentRequest);
        return ResponseEntity.ok(response);
    }
}
