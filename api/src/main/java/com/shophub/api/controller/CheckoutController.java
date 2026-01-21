package com.shophub.api.controller;

import com.shophub.api.model.Order;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.service.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
            @RequestBody Map<String, String> request) {
        try {
            String shippingAddress = request.get("shippingAddress");
            String contactDetails = request.get("contactDetails");
            String paymentMethodStr = request.getOrDefault("paymentMethod", "CREDIT_CARD");

            if (shippingAddress == null || contactDetails == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            PaymentMethod paymentMethod;
            try {
                paymentMethod = PaymentMethod.valueOf(paymentMethodStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                paymentMethod = PaymentMethod.CREDIT_CARD;
            }

            Order order = checkoutService.checkout(userId, shippingAddress, contactDetails, paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
