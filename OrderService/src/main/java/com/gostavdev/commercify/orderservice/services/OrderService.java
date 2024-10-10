package com.gostavdev.commercify.orderservice.services;

import com.gostavdev.commercify.orderservice.dto.OrderDTO;
import com.gostavdev.commercify.orderservice.dto.ProductDto;
import com.gostavdev.commercify.orderservice.dto.api.CreateOrderRequest;
import com.gostavdev.commercify.orderservice.dto.api.OrderLineRequest;
import com.gostavdev.commercify.orderservice.dto.mappers.OrderDTOMapper;
import com.gostavdev.commercify.orderservice.feignclients.ProductsClient;
import com.gostavdev.commercify.orderservice.model.Order;
import com.gostavdev.commercify.orderservice.model.OrderLine;
import com.gostavdev.commercify.orderservice.model.OrderStatus;
import com.gostavdev.commercify.orderservice.repositories.OrderLineRepository;
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
    private final OrderLineRepository orderLineRepository;
    private final OrderDTOMapper mapper;
    private final ProductsClient productsClient;

    @Transactional
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(mapper).toList();
    }

    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        Order order = new Order(request.userId());

        List<OrderLine> orderLines = request.orderLines().stream()
                .collect(Collectors.groupingBy(OrderLineRequest::productId))
                .entrySet().stream()
                .map(entry -> {
                    ProductDto product = productsClient.getProductById(entry.getKey());

                    if (product == null) {
                        throw new RuntimeException("Product not found with ID: " + entry.getKey());
                    }

                    // Create OrderLine entity
                    OrderLine orderLine = new OrderLine();
                    orderLine.setProductId(entry.getKey());
                    orderLine.setProduct(product);
                    orderLine.setQuantity(entry.getValue().stream().mapToInt(OrderLineRequest::quantity).sum());
                    orderLine.setUnitPrice(product.unitPrice());
                    orderLine.setOrder(order);
                    orderLine.setStripeProductId(product.stripeId());

                    return orderLine;
                }).collect(Collectors.toList());

        // Create and save Order entity
        order.setOrderLines(orderLines);

        orderRepository.save(order);
        orderLineRepository.saveAll(orderLines);

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
