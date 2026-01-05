package com.fyn.auth.service.impl;

import com.fyn.auth.dto.request.LoginRequest;
import com.fyn.auth.dto.request.RegisterRequest;
import com.fyn.auth.dto.response.AuthResponse;
import com.fyn.auth.model.User;
import com.fyn.auth.repository.UserRepository;
import com.fyn.auth.security.JwtTokenProvider;
import com.fyn.auth.service.AuthService;
import com.fyn.common.event.user.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import com.fyn.auth.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId().toString())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        try {
            // Publish event to RabbitMQ
            UserRegisteredEvent event = UserRegisteredEvent.builder()
                    .userId(savedUser.getId().toString())
                    .email(savedUser.getEmail())
                    .fullName(request.getFullName())
                    .build();

            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
        } catch (Exception e) {
            // Log but don't fail registration if MQ fails (optional design choice, here
            // we'll just log)
            System.err.println("Failed to send RabbitMQ message: " + e.getMessage());
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());

        return login(loginRequest);
    }
}
