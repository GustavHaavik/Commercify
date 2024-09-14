package com.gostavdev.commercify.paymentservice;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
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