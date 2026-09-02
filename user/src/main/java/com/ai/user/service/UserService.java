package com.ai.user.service;

import com.ai.user.model.User;
import com.ai.user.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ============================
    // GET USER BY ID
    // ============================

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );
    }


    // ============================
    // GET USER BY EMAIL
    // ============================

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }


    // ============================
    // GET ALL USERS
    // ============================

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    // ============================
    // DELETE USER
    // ============================

    public String deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);

        return "User deleted successfully";
    }
}