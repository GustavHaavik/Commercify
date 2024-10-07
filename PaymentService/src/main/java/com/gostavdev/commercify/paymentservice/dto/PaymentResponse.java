package com.gostavdev.commercify.paymentservice.dto;

import com.gostavdev.commercify.paymentservice.entities.PaymentStatus;

public record PaymentResponse(Long paymentId, PaymentStatus status, String redirectUrl) {
    public static PaymentResponse FailedPayment() {
        return new PaymentResponse(-1L, PaymentStatus.FAILED, "");
    }
}

