package com.gostavdev.commercify.paymentservice.services;

import com.gostavdev.commercify.paymentservice.OrderClient;
import com.gostavdev.commercify.paymentservice.entities.PaymentEntity;
import com.gostavdev.commercify.paymentservice.entities.PaymentStatus;
import com.gostavdev.commercify.paymentservice.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    // Get payment status by orderId
    public PaymentStatus getPaymentStatus(Long orderId) {
        Optional<PaymentEntity> payment = paymentRepository.findByOrderId(orderId);
        return payment.map(PaymentEntity::getStatus).orElse(PaymentStatus.NOT_FOUND);
    }
}