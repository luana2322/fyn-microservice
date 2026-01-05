package com.fyn.auth.service;

import com.fyn.auth.dto.request.LoginRequest;
import com.fyn.auth.dto.request.RegisterRequest;
import com.fyn.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
