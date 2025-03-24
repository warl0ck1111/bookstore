package com.example.bookingstore.transaction.models;

/**
 * applications specific payment status
 * all third party transaction/payment status should translate to one of these
 */
public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED
}
