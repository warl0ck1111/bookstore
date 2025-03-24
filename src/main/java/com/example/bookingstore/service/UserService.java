package com.example.bookingstore.service;

import com.example.bookingstore.dto.AuthenticationResponse;
import com.example.bookingstore.dto.CreateUserRequest;
import com.example.bookingstore.dto.LoginRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    AuthenticationResponse registerUser(CreateUserRequest createUserRequest);
    AuthenticationResponse loginUser(LoginRequest loginRequest);
}
