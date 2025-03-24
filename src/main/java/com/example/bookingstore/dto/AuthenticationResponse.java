package com.example.bookingstore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private Long userId;
    private String refreshToken;
    private String accessToken;
    private String username;

}
