package com.example.OrganizationService.client;


import com.example.OrganizationService.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(
            @PathVariable("userId") Long userId
    );
}
