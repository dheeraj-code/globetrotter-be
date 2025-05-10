package com.globetrotter.service;

import com.globetrotter.model.User;

import java.util.Optional;

public interface UserService {
    
    User createUser(String username, String email, String password);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findById(Long id);
    
    void updateScore(Long userId, Integer points);
} 