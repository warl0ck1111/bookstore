package com.example.bookingstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateUserRequest {
    private @NotBlank(message = "invalid username")
    String username;

    private @NotBlank(message = "password field can not be empty")
    String password;

    private @NotBlank(message = "confirm password field can not be empty")
    String confirmPassword;
}
