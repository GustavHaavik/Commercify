package com.gostavdev.commercify.orderservice;

import com.gostavdev.commercify.orderservice.dto.CreateOrderRequest;
import com.gostavdev.commercify.orderservice.dto.CreatePaymentRequest;
import com.gostavdev.commercify.orderservice.dto.PaymentResponse;
import com.gostavdev.commercify.orderservice.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private static final String PAYMENTS_SERVICE_URI = "http://localhost:8080/api/v1/payments";
    private static final String PRODUCTS_SERVICE_URI = "http://localhost:8080/api/v1/products";

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

    public Order createOrder(CreateOrderRequest orderRequest) {
        Order order = new Order(orderRequest);

        // Check product availability by calling Product Service
        ProductDto product = restTemplate.getForObject(String.format("%s/%s", PRODUCTS_SERVICE_URI, order.getProductId()), ProductDto.class);
        if (product == null || product.stock() < order.getQuantity()) {
            throw new RuntimeException("Product not available or insufficient quantity");
        }
        
        // Calculate total price
        double totalPrice = product.price() * orderRequest.quantity();
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Communicate with Payment Service to process payment (simplified)
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest(savedOrder.getId(), savedOrder.getTotalPrice(), "USD");
        restTemplate.postForObject(String.format("%s/create-payment-intent", PAYMENTS_SERVICE_URI), paymentRequest, PaymentResponse.class);

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
