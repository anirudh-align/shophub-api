package com.shophub.api.controller;

import com.shophub.api.dto.LoginRequest;
import com.shophub.api.dto.RegisterRequest;
import com.shophub.api.exception.ResourceNotFoundException;
import com.shophub.api.model.User;
import com.shophub.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());
        // TODO: Return JWT token instead of user object
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestParam UUID userId) {
        // TODO: Extract userId from JWT token
        User user = authService.getCurrentUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return ResponseEntity.ok(user);
    }
}
