package com.gostavdev.commercify.orderservice.dto.mappers;

import com.gostavdev.commercify.orderservice.dto.OrderDTO;
import com.gostavdev.commercify.orderservice.dto.OrderLineDTO;
import com.gostavdev.commercify.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderDTOMapper implements Function<Order, OrderDTO> {
    private final OrderLineDTOMapper olMapper;

    @Override
    public OrderDTO apply(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setOrderStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        List<OrderLineDTO> orderLines = order.getOrderLines().stream()
                .map(olMapper)
                .toList();

        dto.setOrderLines(orderLines);

        return dto;
    }
}
