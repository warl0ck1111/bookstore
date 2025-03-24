package com.example.bookingstore.transaction;

import com.example.bookingstore.entity.Order;
import com.example.bookingstore.enums.PaymentMethod;
import com.example.bookingstore.transaction.models.*;

import java.util.List;

public interface TransactionService {

    VerifyTransactionResponse verifyTransaction(String paymentReference);

    boolean processOrderPayment(Order order, PaymentMethod paymentMethod, PaymentChannel paymentChannel);

    List<TransactionResponse> getPurchaseHistory(Long userId);

    TransactionResponse getTransactionDetails(String transactionId);

}
