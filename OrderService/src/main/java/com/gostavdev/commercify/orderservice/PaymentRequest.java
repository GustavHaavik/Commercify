package com.gostavdev.commercify.orderservice;

public class PaymentRequest {
    private Long orderId;
    private Double amount;
    private String currency;

    public PaymentRequest() {
    }

    public PaymentRequest(Long orderId, Double amount, String currency) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
