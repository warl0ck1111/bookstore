package com.example.bookingstore.service;

import com.example.bookingstore.dto.responses.AuthenticationResponse;
import com.example.bookingstore.dto.request.CreateUserRequest;
import com.example.bookingstore.dto.request.LoginRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    AuthenticationResponse registerUser(CreateUserRequest createUserRequest);
    AuthenticationResponse loginUser(LoginRequest loginRequest);
}
