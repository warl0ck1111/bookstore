package com.example.bookingstore.transaction.models;

import com.example.bookingstore.entity.Order;
import com.example.bookingstore.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {
    private Order order;
    private String description;
    private PaymentChannel paymentChannel;
    private PaymentMethod paymentMethod;
    private String userName;
    private Long userId;
    private String transactionId;
    private BigDecimal amount;
    private Currency currency;

    public TransactionRequest(Order order, String transactionId, Long userId, String description, PaymentChannel paymentChannel, PaymentMethod paymentMethod, BigDecimal amount, String userName, Currency currency) {
        this.order = order;
        this.transactionId = transactionId;
        this.userId = userId;
        this.description = description;
        this.paymentChannel = paymentChannel;
        this.amount = amount;
        this.userName = userName;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }
}
