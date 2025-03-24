package com.example.bookingstore.transaction.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VerifyTransactionResponse {
    private String transactionId;
    private boolean success;
    private String transactionStatus;
    private BigDecimal transactionAmount;
    private String message;
}
