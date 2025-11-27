package com.purrfect.auth_service.service;

import java.util.List;
import java.util.Optional;

import com.purrfect.auth_service.domain.User;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(User user);
}
