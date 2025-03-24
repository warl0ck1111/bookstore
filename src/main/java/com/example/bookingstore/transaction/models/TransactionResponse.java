package com.example.bookingstore.transaction.models;


import com.example.bookingstore.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class TransactionResponse {
    private String transactionId;
    private BigDecimal amount;
    private Currency currency;
    private PaymentMethod paymentMethod;
    private PaymentChannel paymentChannel;
    private LocalDateTime timestamp;
    private TransactionStatus status;
}
