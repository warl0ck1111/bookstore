package com.example.bookingstore.exceptions;

public class BookServiceException extends RuntimeException {
    public BookServiceException(String message) {
        super(message);
    }
}
