package com.example.bookingstore.controller;


import com.example.bookingstore.dto.responses.AuthenticationResponse;
import com.example.bookingstore.dto.request.CreateUserRequest;
import com.example.bookingstore.dto.request.LoginRequest;
import com.example.bookingstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        AuthenticationResponse authenticationResponse = userService.registerUser(createUserRequest);
        return ResponseEntity.ok(authenticationResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest request){
        AuthenticationResponse authenticationResponse = userService.loginUser(request);
        return ResponseEntity.ok(authenticationResponse);

    }
}
