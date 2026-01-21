package com.shophub.api.controller;

import com.shophub.api.model.Order;
import com.shophub.api.model.enums.OrderStatus;
import com.shophub.api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable UUID userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<Map<String, String>> getOrderStatus(@PathVariable UUID orderId) {
        try {
            OrderStatus status = orderService.getOrderStatus(orderId);
            return ResponseEntity.ok(Map.of("status", status.toString()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
