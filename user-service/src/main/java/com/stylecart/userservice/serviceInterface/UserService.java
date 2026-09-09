package com.stylecart.userservice.serviceInterface;

import com.stylecart.userservice.dto.UserRequest;
import com.stylecart.userservice.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
}
