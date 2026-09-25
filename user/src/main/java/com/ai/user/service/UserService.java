package com.ai.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ai.user.dto.UserResponse;
import com.ai.user.model.User;
import com.ai.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET USER BY ID
    // =========================================================

    public User getUserById(Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );
    }

    // =========================================================
    // GET USER BY EMAIL
    // Used to identify the logged-in user from JWT
    // =========================================================

    public User getUserByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email
                        )
                );
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<UserResponse> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    public String deleteUser(Long id) {

        if (!userRepository.existsById(id)) {

            throw new RuntimeException(
                    "User not found with id: " + id
            );
        }

        userRepository.deleteById(id);

        return "User deleted successfully";
    }

    // =========================================================
    // CONVERT USER → USER RESPONSE
    // Never expose password
    // =========================================================

    private UserResponse convertToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getOrganizationId()
        );
    }
}