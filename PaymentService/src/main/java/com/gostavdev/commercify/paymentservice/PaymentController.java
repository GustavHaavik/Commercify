package com.gostavdev.commercify.paymentservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RestTemplate restTemplate;

    public PaymentController(PaymentService paymentService, RestTemplate restTemplate) {
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
    }

    // Endpoint to process a payment
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.processPayment(paymentRequest.getOrderId(), paymentRequest.getAmount(), paymentRequest.getPaymentMethod());

        Long orderId = paymentRequest.getOrderId();

        // After successful payment
        restTemplate.postForObject("http://ORDER-SERVICE/orders/" + orderId + "/status", new OrderStatusUpdate("PAID"), Void.class);

        return ResponseEntity.ok(payment);
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