package com.shophub.api.controller;

import com.shophub.api.dto.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = new HealthResponse();
        response.setStatus("UP");
        response.setService("ShopHub Backend");
        response.setTimestamp(LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}
