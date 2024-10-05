package com.gostavdev.commercify.paymentservice.dto;

import com.gostavdev.commercify.paymentservice.entities.PaymentStatus;

public class PaymentStatusRequest {
    private PaymentStatus status;

    public PaymentStatusRequest(PaymentStatus status) {
        this.status = status;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}