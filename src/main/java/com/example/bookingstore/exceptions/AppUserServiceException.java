package com.example.bookingstore.exceptions;

public class AppUserServiceException extends RuntimeException {
    public AppUserServiceException(String message) {
        super(message);
    }
}
