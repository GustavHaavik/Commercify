package com.gostavdev.commercify.paymentservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long orderId,
        Long userId,
        String orderStatus,
        List<OrderLineDTO> orderLines,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
