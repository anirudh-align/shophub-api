package com.shophub.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/mock")
    public ResponseEntity<Map<String, Object>> mockPayment(@RequestBody Map<String, Object> request) {
        // TODO: Integrate with actual payment gateway
        // Mock payment - always returns success
        Map<String, Object> response = Map.of(
            "status", "SUCCESS",
            "message", "Payment processed successfully",
            "transactionId", UUID.randomUUID().toString()
        );
        return ResponseEntity.ok(response);
    }
}
