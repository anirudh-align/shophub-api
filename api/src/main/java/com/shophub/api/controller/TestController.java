package com.shophub.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/echo/{message}")
    public ResponseEntity<Map<String, String>> echo(@PathVariable String message) {
        Map<String, String> response = new HashMap<>();
        response.put("echo", message);
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "ShopHub Backend");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("javaVersion", System.getProperty("java.version"));
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echoPost(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("received", body != null ? body : "No body provided");
        response.put("method", "POST");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/headers")
    public ResponseEntity<Map<String, Object>> headers(@RequestHeader Map<String, String> headers) {
        Map<String, Object> response = new HashMap<>();
        response.put("headers", headers);
        response.put("headerCount", headers.size());
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
