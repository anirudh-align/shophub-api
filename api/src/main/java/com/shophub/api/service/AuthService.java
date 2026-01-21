package com.shophub.api.service;

import com.shophub.api.model.User;
import com.shophub.api.model.enums.UserRole;
import com.shophub.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(String name, String email, String password) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email already exists: " + email);
        }

        // TODO: Hash password before saving
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // TODO: Use BCrypt or similar
        user.setRole(UserRole.CUSTOMER);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        // TODO: Implement proper authentication with JWT
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // TODO: Verify password hash
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    public Optional<User> getCurrentUser(UUID userId) {
        return userRepository.findById(userId);
    }
}
