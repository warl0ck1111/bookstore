package com.example.bookingstore.transaction;

import com.example.bookingstore.entity.Order;
import com.example.bookingstore.enums.PaymentMethod;
import com.example.bookingstore.transaction.exceptions.TransactionNotFoundException;
import com.example.bookingstore.transaction.models.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.example.bookingstore.enums.PaymentMethod.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    public static final String PAYMENT_SUCCESSFUL_MESSAGE = "payment was successful";
    private final TransactionRepository transactionRepository;


    @Override
    public VerifyTransactionResponse verifyTransaction(String paymentReference) {
        log.info("verifyTransaction/paymentReference:{}", paymentReference);
        Transaction transaction = findById(paymentReference);
        boolean success = transaction.getTransactionStatus() == TransactionStatus.COMPLETED;
        return VerifyTransactionResponse.builder()
                .transactionId(paymentReference)
                .success(success)
                .transactionAmount(transaction.getAmount())
                .transactionStatus(transaction.getTransactionStatus().toString())
                .message(success ? PAYMENT_SUCCESSFUL_MESSAGE : "Payment failed")
                .build();
    }

    @Override
    public boolean processOrderPayment(Order order, PaymentMethod paymentMethod, PaymentChannel paymentChannel) {
        log.info("processOrderPayment/orderId:{} paymentMethod:{} paymentChannel:{}", order.getId(), paymentMethod, paymentChannel);
        log.info("Simulating payment processing...");
        TransactionStatus transactionStatus;
        switch (paymentMethod) {
            case WEB -> transactionStatus = processWebPayment(order, paymentChannel);
            case USSD -> transactionStatus = processUSSDPayment(order, paymentChannel);
            case TRANSFER -> transactionStatus = processTransferPayment(order, paymentChannel, paymentMethod);
            default -> throw new IllegalArgumentException("invalid payment method");
        }
        return transactionStatus == TransactionStatus.COMPLETED;
    }

    @Override
    public List<TransactionResponse> getPurchaseHistory(Long userId) {
        log.info("getPurchaseHistory/userId:{}", userId);
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
                .map(transaction -> TransactionResponse.builder()
                        .transactionId(transaction.getId())
                        .status(transaction.getTransactionStatus())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .paymentMethod(transaction.getPaymentMethod())
                        .paymentChannel(transaction.getPaymentChannel())
                        .timestamp(transaction.getDateCreated())
                        .build())
                .toList();
    }

    @Override
    public TransactionResponse getTransactionDetails(String transactionId) {
        log.info("getTransactionDetails/transactionId:{}", transactionId);
        Transaction transaction = findById(transactionId);

        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .status(transaction.getTransactionStatus())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod())
                .paymentChannel(transaction.getPaymentChannel())
                .timestamp(transaction.getDateCreated())
                .build();
    }


    private TransactionStatus processWebPayment(Order order, PaymentChannel paymentChannel) {
        log.info("processWebPayment/orderId:{}", order.getId());
        TransactionRequest request = buildTransactionRequest(order, paymentChannel, WEB);
        Transaction transaction = createTransaction(request);
        return updateTransactionStatus(transaction, simulatePaymentSuccess());
    }

    private TransactionStatus processUSSDPayment(Order order, PaymentChannel paymentChannel) {
        log.info("processUSSDPayment/orderId:{}", order.getId());
        TransactionRequest request = buildTransactionRequest(order, paymentChannel, USSD);
        Transaction transaction = createTransaction(request);
        return updateTransactionStatus(transaction, simulatePaymentSuccess());
    }

    private TransactionStatus processTransferPayment(Order order, PaymentChannel paymentChannel, PaymentMethod paymentMethod) {
        log.info("processTransferPayment/orderId:{}", order.getId());
        TransactionRequest request = buildTransactionRequest(order, paymentChannel, paymentMethod);
        Transaction transaction = createTransaction(request);
        return updateTransactionStatus(transaction, simulatePaymentSuccess());
    }

    private TransactionRequest buildTransactionRequest(Order order, PaymentChannel paymentChannel, PaymentMethod paymentMethod) {
        return new TransactionRequest(
                order,
                UUID.randomUUID().toString(),
                order.getUser().getId(),
                "PAYMENT FOR ORDER",
                paymentChannel,
                paymentMethod,
                order.getTotalPrice(),
                order.getUser().getUsername(),
                Currency.NGN
        );
    }

    private Transaction createTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = Transaction.builder()
                .subjectReference(UUID.randomUUID().toString())
                .userName(transactionRequest.getUserName())
                .order(transactionRequest.getOrder())
                .userId(transactionRequest.getUserId())
                .description(transactionRequest.getDescription())
                .currency(transactionRequest.getCurrency())
                .amount(transactionRequest.getAmount())
                .paymentReference(UUID.randomUUID().toString())
                .paymentMethod(transactionRequest.getPaymentMethod())
                .paymentChannel(transactionRequest.getPaymentChannel())
                .transactionStatus(TransactionStatus.PENDING)
                .build();
        return transactionRepository.save(transaction);
    }



    private Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("transaction not found"));
    }

    private TransactionStatus updateTransactionStatus(Transaction transaction, boolean success) {
        log.info("updateTransactionStatus/transactionId:{}  success:{}", transaction.getId(), success);
        TransactionStatus status = success ? TransactionStatus.COMPLETED : TransactionStatus.FAILED;
        transactionRepository.updateTransactionStatusAndPaymentReferenceById(transaction.getId(), transaction.getPaymentReference(), status);
        return status;
    }

    private boolean simulatePaymentSuccess() {
        log.info("simulatePaymentSuccess...");
//        Simulate a 90% success rate
        return new Random().nextInt(10) < 9;
    }
}