package com.shophub.api.service;

import com.shophub.api.model.User;
import com.shophub.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }
}
