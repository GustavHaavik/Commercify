package com.gostavdev.commercify.orderservice.services;

import com.gostavdev.commercify.orderservice.feignclients.ProductsClient;
import com.gostavdev.commercify.orderservice.model.Order;
import com.gostavdev.commercify.orderservice.model.OrderLine;
import com.gostavdev.commercify.orderservice.model.OrderStatus;
import com.gostavdev.commercify.orderservice.dto.ProductDto;
import com.gostavdev.commercify.orderservice.exceptions.OrderNotFoundException;
import com.gostavdev.commercify.orderservice.exceptions.ProductException;
import com.gostavdev.commercify.orderservice.repositories.OrderLineRepository;
import com.gostavdev.commercify.orderservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductsClient productsClient;

    private static final String PAYMENTS_SERVICE_URI = "http://localhost:8080/api/v1/payments";
    private static final String PRODUCTS_SERVICE_URI = "http://localhost:8080/api/v1/products";

    public OrderService(OrderRepository orderRepository, OrderLineRepository orderLineRepository, ProductsClient productsClient) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.productsClient = productsClient;
    }

    public Order getOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Long userId, List<OrderLine> orderLines) {
        Order order = new Order(userId);

        // Check product availability by calling Product Service
        // TODO: Might not be the best approach, but it's a start
        for (OrderLine orderLine : orderLines) {
            System.out.println("Checking product availability for product " + orderLine.getProductId());
            ProductDto product = productsClient.getProduct(orderLine.getProductId());

            if (product == null)
                throw new ProductException("Product not found");
            if (product.stock() < orderLine.getQuantity())
                throw new ProductException("Product not available");

            orderLine.setOrder(order);
            orderLine.setProduct(product);
        }

        Order savedOrder = orderRepository.save(order);
        orderLineRepository.saveAll(orderLines);

        System.out.println("Order created: " + savedOrder.calculateTotalPrice());

        // Communicate with Payment Service to process payment (simplified)
//        CreatePaymentRequest paymentRequest = new CreatePaymentRequest(savedOrder.getId(), savedOrder.calculateTotalPrice(), "USD");
//        restTemplate.postForObject(String.format("%s/create-payment-intent", PAYMENTS_SERVICE_URI), paymentRequest, PaymentResponse.class);

        System.out.println("Order created: " + savedOrder.getId());

        return savedOrder;
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order;
        try {
            order = getOrderById(orderId);
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e);
        }
        order.updateStatus(status);
        orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
