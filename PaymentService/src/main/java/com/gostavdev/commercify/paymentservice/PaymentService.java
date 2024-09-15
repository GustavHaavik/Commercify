package com.gostavdev.commercify.paymentservice;

import com.gostavdev.commercify.paymentservice.exceptions.InvalidEventDataException;
import com.gostavdev.commercify.paymentservice.exceptions.PaymentNotFoundException;
import com.gostavdev.commercify.paymentservice.model.Payment;
import com.gostavdev.commercify.paymentservice.model.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
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
        Payment payment = new Payment(orderId, amount, "Stripe", PaymentStatus.PENDING, LocalDateTime.now());
        paymentRepository.save(payment);

        // Return the payment intent's client secret for client-side confirmation
        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }

    // Update payment status after confirmation (if needed)
    public void updatePaymentStatus(Long orderId, PaymentStatus status) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
        payment.setStatus(status);
        paymentRepository.save(payment);
    }

    // Process a new payment
    public Payment processPayment(Long orderId, Double amount, String paymentMethod) {
        // Assume payment is successful (real implementation will involve actual payment gateway)
        Payment payment = new Payment(orderId, amount, paymentMethod, PaymentStatus.PAID, LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    // Get payment status by orderId
    public PaymentStatus getPaymentStatus(Long orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        return payment.map(Payment::getStatus).orElse(PaymentStatus.NOT_FOUND);
    }

    // Refund payment (for simplicity, assume refund always succeeds)
    public Payment refundPayment(Long orderId) throws PaymentNotFoundException {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(PaymentStatus.REFUNDED);
            return paymentRepository.save(payment);
        }
        throw new PaymentNotFoundException("Payment not found for order ID: " + orderId);
    }

    // Handle successful payments
    public void handlePaymentSucceeded(Event event) {
        // Extract the PaymentIntent object from the event
        Optional<StripeObject> dataObject = event.getDataObjectDeserializer().getObject();
        if (dataObject.isEmpty() || !(dataObject.get() instanceof PaymentIntent paymentIntent))
            throw new InvalidEventDataException("Invalid event data object");

        Long orderId = Long.parseLong(paymentIntent.getMetadata().get("orderId"));

        // Update payment status in the database
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
        }
    }

    // Handle failed payments
    public void handlePaymentFailed(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();

        Long orderId = Long.parseLong(paymentIntent.getMetadata().get("orderId"));

        // Update payment status in the database
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }
    }
}