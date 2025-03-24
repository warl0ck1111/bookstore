package com.example.bookingstore.dto;


import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "username field can not be empty")String username, @NotBlank(message = "password field can not be empty")String password) {
}
