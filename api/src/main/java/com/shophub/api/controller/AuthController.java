package com.shophub.api.controller;

import com.shophub.api.dto.LoginRequest;
import com.shophub.api.dto.LoginResponse;
import com.shophub.api.dto.RegisterRequest;
import com.shophub.api.exception.ResourceNotFoundException;
import com.shophub.api.model.User;
import com.shophub.api.security.CustomUserDetailsService;
import com.shophub.api.security.JwtUtil;
import com.shophub.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthService authService, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = authService.login(request.getEmail(), request.getPassword());
            
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());
            
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId().toString(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().toString()
            );
            
            LoginResponse response = new LoginResponse(token, userInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Re-throw to be handled by GlobalExceptionHandler
            throw e;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Load user by email (email is stored as username in UserDetails)
        User currentUser = authService.getUserByEmail(email);
        
        return ResponseEntity.ok(currentUser);
    }
}
