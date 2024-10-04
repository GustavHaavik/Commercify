package com.gostavdev.commercify.orderservice.services;

import com.gostavdev.commercify.orderservice.dto.OrderDTO;
import com.gostavdev.commercify.orderservice.dto.api.CreateOrderRequest;
import com.gostavdev.commercify.orderservice.dto.mappers.OrderDTOMapper;
import com.gostavdev.commercify.orderservice.model.Order;
import com.gostavdev.commercify.orderservice.model.OrderStatus;
import com.gostavdev.commercify.orderservice.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDTOMapper mapper;

    @Transactional
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(mapper).toList();
    }

    @Transactional
    public OrderDTO createOrder(CreateOrderRequest orderRequest) {
        Order order = new Order(orderRequest.userId());
        orderRepository.save(order);

        return mapper.apply(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.updateStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public OrderDTO getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(mapper)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
    
    @Transactional
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(mapper).collect(Collectors.toList());
    }
}
