package com.stylecart.authservice.client;

import com.stylecart.authservice.dto.CreateUserRequest;
import com.stylecart.authservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/api/users")
    UserResponse createUser(@RequestBody CreateUserRequest request);
}