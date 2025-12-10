package com.example.pweb_backend.service;

import com.example.pweb_backend.dto.LoginRequest;
import com.example.pweb_backend.dto.RegisterRequest;
import com.example.pweb_backend.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse login(LoginRequest request);
}
