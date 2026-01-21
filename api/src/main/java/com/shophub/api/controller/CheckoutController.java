package com.shophub.api.controller;

import com.shophub.api.dto.CheckoutRequest;
import com.shophub.api.model.Order;
import com.shophub.api.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<Order> checkout(
            @PathVariable UUID userId,
            @Valid @RequestBody CheckoutRequest request) {
        Order order = checkoutService.checkout(
                userId,
                request.getShippingAddress(),
                request.getContactDetails(),
                request.getPaymentMethod()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
