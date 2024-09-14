package com.gostavdev.commercify.orderservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public Order getOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Order order) {
        // Check product availability by calling Product Service
        ProductDto product = restTemplate.getForObject("http://PRODUCT-SERVICE/products/" + order.getProductId(), ProductDto.class);
        if (product == null || product.getQuantity() < order.getQuantity()) {
            throw new RuntimeException("Product not available or insufficient quantity");
        }

        // Calculate total price
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Communicate with Payment Service to process payment (simplified)
        PaymentRequest paymentRequest = new PaymentRequest(savedOrder.getId(), savedOrder.getTotalPrice().doubleValue(), "USD");
        restTemplate.postForObject("http://PAYMENT-SERVICE/create-payment-intent", paymentRequest, PaymentResponse.class);

        return savedOrder;
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order;
        try {
            order = getOrderById(orderId);
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e);
        }
        order.setStatus(status);
        orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
