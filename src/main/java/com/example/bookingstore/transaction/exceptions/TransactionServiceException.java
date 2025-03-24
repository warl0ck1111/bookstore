package com.example.bookingstore.transaction.exceptions;

public class TransactionServiceException extends RuntimeException {
    public TransactionServiceException(String message) {
        super(message);
    }
}
