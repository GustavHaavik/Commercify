package com.gostavdev.commercify.paymentservice;

import com.gostavdev.commercify.paymentservice.exceptions.PaymentNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
        String payload;
        String sigHeader = request.getHeader("Stripe-Signature");

        // Read the payload (body) of the webhook request
        try (BufferedReader reader = request.getReader()) {
            payload = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error reading payload");
        }

        Event event;
        try {
            // Verify and construct the event
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Webhook signature verification failed");
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                // Handle successful payment
                paymentService.handlePaymentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                // Handle failed payment
                paymentService.handlePaymentFailed(event);
                break;
            // Add more cases as needed for other events like refunds, disputes, etc.
            default:
                return ResponseEntity.ok("Unhandled event type");
        }

        return ResponseEntity.ok("Event processed");
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
        try {
            paymentService.updatePaymentStatus(orderId, request.getStatus());
        } catch (PaymentNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    // Endpoint to get the status of a payment
    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getPaymentStatus(@PathVariable Long orderId) {
        PaymentStatus status = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(status.name());
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