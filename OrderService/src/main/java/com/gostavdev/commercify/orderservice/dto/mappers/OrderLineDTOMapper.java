package com.gostavdev.commercify.orderservice.dto.mappers;

import com.gostavdev.commercify.orderservice.dto.OrderLineDTO;
import com.gostavdev.commercify.orderservice.model.OrderLine;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderLineDTOMapper implements Function<OrderLine, OrderLineDTO> {
    @Override
    public OrderLineDTO apply(OrderLine orderLine) {
        return new OrderLineDTO(
                orderLine.getProductId(),
                orderLine.getStripeProductId(),
                orderLine.getQuantity(),
                orderLine.getUnitPrice()
        );
    }
}
