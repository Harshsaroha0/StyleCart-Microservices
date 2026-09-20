package com.stylecart.authservice.service;

import com.stylecart.authservice.client.UserServiceClient;
import com.stylecart.authservice.dto.CreateUserRequest;
import com.stylecart.authservice.dto.LoginRequest;
import com.stylecart.authservice.dto.LoginResponse;
import com.stylecart.authservice.dto.RegisterRequest;
import com.stylecart.authservice.entity.AuthUser;
import com.stylecart.authservice.entity.Role;
import com.stylecart.authservice.repository.AuthUserRepository;
import com.stylecart.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        AuthUser authUser = new AuthUser();

        authUser.setEmail(request.getEmail());
        authUser.setPassword(hashedPassword);
        authUser.setRole(Role.USER);
        authUser.setEnabled(true);

        authUserRepository.save(authUser);

        CreateUserRequest userRequest = new CreateUserRequest();

        userRequest.setFirstName(request.getFirstName());
        userRequest.setLastName(request.getLastName());
        userRequest.setEmail(request.getEmail());
        userRequest.setPhoneNumber(request.getPhoneNumber());

        userServiceClient.createUser(userRequest);
    }

    public LoginResponse login(LoginRequest request) {

        AuthUser authUser = authUserRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        if (!authUser.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                authUser.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                authUser.getEmail(),
                authUser.getRole().name()
        );

        return new LoginResponse(token);
    }
}