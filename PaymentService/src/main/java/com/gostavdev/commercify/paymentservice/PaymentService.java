package com.gostavdev.commercify.paymentservice;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Process payment using Stripe
    public Map<String, Object> processStripePayment(Long orderId, Double amount, String currency) throws StripeException {
        // Convert amount to cents (Stripe works with integer values for the amount)
        long amountInCents = (long) (amount * 100);

        // Create a payment intent
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency(currency)
                        .putMetadata("orderId", orderId.toString())
                        .build();

        // Create the PaymentIntent
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Save the payment in our local database with status "PENDING"
        Payment payment = new Payment(orderId, amount, "Stripe", "PENDING", LocalDateTime.now());
        paymentRepository.save(payment);

        // Return the payment intent's client secret for client-side confirmation
        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }

    // Update payment status after confirmation (if needed)
    public void updatePaymentStatus(Long orderId, String status) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        paymentRepository.save(payment);
    }

    // Process a new payment
    public Payment processPayment(Long orderId, Double amount, String paymentMethod) {
        // Assume payment is successful (real implementation will involve actual payment gateway)
        Payment payment = new Payment(orderId, amount, paymentMethod, "COMPLETED", LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    // Get payment status by orderId
    public String getPaymentStatus(Long orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        return payment.map(Payment::getStatus).orElse("Payment not found");
    }

    // Refund payment (for simplicity, assume refund always succeeds)
    public Payment refundPayment(Long orderId) throws PaymentNotFoundException {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus("REFUNDED");
            return paymentRepository.save(payment);
        }
        throw new PaymentNotFoundException("Payment not found for order ID: " + orderId);
    }

}