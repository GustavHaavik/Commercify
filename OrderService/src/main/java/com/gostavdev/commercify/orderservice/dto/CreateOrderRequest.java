package com.gostavdev.commercify.orderservice.dto;

import com.gostavdev.commercify.orderservice.model.OrderLine;

import java.util.List;

public record CreateOrderRequest(Long userId, List<OrderLine> orderLines) {
    public CreateOrderRequest {
        if (userId == null)
            throw new IllegalArgumentException("userId cannot be null");

        if (orderLines == null || orderLines.isEmpty())
            throw new IllegalArgumentException("orderLines cannot be null or empty");
    }
}
