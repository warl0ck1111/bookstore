package com.example.bookingstore.exceptions;

public class AuthorServiceException extends RuntimeException {
    public AuthorServiceException(String message) {
        super(message);
    }
}
