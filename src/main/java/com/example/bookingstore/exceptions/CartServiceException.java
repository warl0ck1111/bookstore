package com.example.bookingstore.exceptions;

public class CartServiceException extends RuntimeException {
    public CartServiceException(String message) {
        super(message);
    }
}
