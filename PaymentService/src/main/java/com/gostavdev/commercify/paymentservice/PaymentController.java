package com.gostavdev.commercify.paymentservice;

import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Endpoint to initiate a payment with Stripe
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        try {
            Map<String, Object> response = paymentService.processStripePayment(
                    paymentRequest.getOrderId(),
                    paymentRequest.getAmount(),
                    paymentRequest.getCurrency()
            );
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint to update payment status (if needed)
    @PostMapping("/{orderId}/update-status")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long orderId, @RequestBody PaymentStatusRequest request) {
        paymentService.updatePaymentStatus(orderId, request.getStatus());
        return ResponseEntity.ok().build();
    }

    // Endpoint to get the status of a payment
    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getPaymentStatus(@PathVariable Long orderId) {
        String status = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(status);
    }

    // Endpoint to refund a payment
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long orderId) {
        Payment payment;
        try {
            payment = paymentService.refundPayment(orderId);
        } catch (PaymentNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(payment);
    }
}