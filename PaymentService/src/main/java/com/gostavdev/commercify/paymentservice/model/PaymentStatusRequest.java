package com.gostavdev.commercify.paymentservice.model;

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