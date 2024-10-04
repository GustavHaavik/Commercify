package com.gostavdev.commercify.orderservice.dto.api;

import com.gostavdev.commercify.orderservice.model.OrderLine;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        List<OrderLine> orderLines) {
}
