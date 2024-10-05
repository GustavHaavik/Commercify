package com.gostavdev.commercify.paymentservice.services;

import com.gostavdev.commercify.paymentservice.exceptions.InvalidEventDataException;
import com.gostavdev.commercify.paymentservice.exceptions.PaymentNotFoundException;
import com.gostavdev.commercify.paymentservice.entities.Payment;
import com.gostavdev.commercify.paymentservice.dto.PaymentRequest;
import com.gostavdev.commercify.paymentservice.dto.PaymentResponse;
import com.gostavdev.commercify.paymentservice.entities.PaymentStatus;
import com.gostavdev.commercify.paymentservice.repositories.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Process payment using Stripe
    public PaymentResponse processStripePayment(PaymentRequest paymentRequest) {
        // Convert amount to cents (Stripe works with integer values for the amount)
        long amountInCents = (long) (paymentRequest.amount() * 100);

        System.out.println("Processing payment for order " + paymentRequest.orderId() + " using Stripe");
        System.out.println("Payment amount: " + amountInCents + " cents");

        // Create a payment intent
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency(paymentRequest.currency())
                        .putMetadata("orderId", paymentRequest.orderId().toString())
                        .build();

        try {
            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save the payment in our local database with status "PENDING"
            Payment payment = new Payment(paymentRequest);
            paymentRepository.save(payment);

            // Return the payment intent's client secret for client-side confirmation
            return new PaymentResponse(payment.getPaymentId(), payment.getStatus(), paymentIntent.getClientSecret());
        } catch (StripeException e) {
            System.out.println("Error processing payment: " + e.getMessage());
            return PaymentResponse.FailedPayment();
        }
    }

    // Get payment status by orderId
    public PaymentStatus getPaymentStatus(Long orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        return payment.map(Payment::getStatus).orElse(PaymentStatus.NOT_FOUND);
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