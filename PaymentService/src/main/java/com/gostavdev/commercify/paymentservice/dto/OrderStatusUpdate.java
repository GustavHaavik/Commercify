package com.gostavdev.commercify.paymentservice.dto;

public class OrderStatusUpdate {
    private String status;

    public OrderStatusUpdate() {
    }

    public OrderStatusUpdate(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}