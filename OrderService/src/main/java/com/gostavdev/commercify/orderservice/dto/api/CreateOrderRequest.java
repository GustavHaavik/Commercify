package com.gostavdev.commercify.orderservice.dto.api;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        List<OrderLineRequest> orderLines) {
}
