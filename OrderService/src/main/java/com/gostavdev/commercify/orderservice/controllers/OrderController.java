package com.gostavdev.commercify.orderservice.controllers;

import com.gostavdev.commercify.orderservice.dto.CreateOrderRequest;
import com.gostavdev.commercify.orderservice.model.Order;
import com.gostavdev.commercify.orderservice.model.OrderStatus;
import com.gostavdev.commercify.orderservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Endpoint to create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest.userId(), orderRequest.orderLines());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // Endpoint to fetch orders by user ID
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        System.out.println("Fetching orders for user ID: " + userId);
        return orderService.getOrdersByUserId(userId);
    }

    // Endpoint to fetch an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order;
        try {
            order = orderService.getOrderById(id);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(new Order());
        }
        return ResponseEntity.ok(order);
    }

    // Endpoint to update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        orderService.updateOrderStatus(id, orderStatus);
        return ResponseEntity.ok().build();
    }
}
