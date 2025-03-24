package com.example.bookingstore.transaction;

import com.example.bookingstore.entity.Order;
import com.example.bookingstore.enums.PaymentMethod;
import com.example.bookingstore.transaction.exceptions.TransactionNotFoundException;
import com.example.bookingstore.transaction.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test order
        Order testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setTotalPrice(BigDecimal.valueOf(200.00));

        // Create test transaction
        testTransaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .order(testOrder)
                .transactionStatus(TransactionStatus.COMPLETED)
                .amount(BigDecimal.valueOf(200.00))
                .paymentMethod(PaymentMethod.WEB)
                .currency(Currency.NGN)
                .paymentReference(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void shouldVerifyTransactionSuccessfully() {
        // Given
        when(transactionRepository.findById(testTransaction.getId())).thenReturn(Optional.of(testTransaction));

        // When
        VerifyTransactionResponse response = transactionService.verifyTransaction(testTransaction.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getTransactionStatus()).isEqualTo(TransactionStatus.COMPLETED.toString());

        verify(transactionRepository, times(1)).findById(testTransaction.getId());
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        // Given
        String invalidTransactionId = "invalid-123";
        when(transactionRepository.findById(invalidTransactionId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> transactionService.verifyTransaction(invalidTransactionId))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("transaction not found");

        verify(transactionRepository, times(1)).findById(invalidTransactionId);
    }

    @Test
    void shouldRetrievePurchaseHistory() {
        // Given
        when(transactionRepository.findByUserId(1L)).thenReturn(List.of(testTransaction));

        // When
        List<TransactionResponse> history = transactionService.getPurchaseHistory(1L);

        // Then
        assertThat(history).isNotEmpty();
        assertThat(history.getFirst().getTransactionId()).isEqualTo(testTransaction.getId());
        assertThat(history.getFirst().getStatus()).isEqualTo(TransactionStatus.COMPLETED);

        verify(transactionRepository, times(1)).findByUserId(1L);
    }

    @Test
    void shouldRetrieveTransactionDetails() {
        // Given
        when(transactionRepository.findById(testTransaction.getId())).thenReturn(Optional.of(testTransaction));

        // When
        TransactionResponse response = transactionService.getTransactionDetails(testTransaction.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTransactionId()).isEqualTo(testTransaction.getId());
        assertThat(response.getStatus()).isEqualTo(TransactionStatus.COMPLETED);

        verify(transactionRepository, times(1)).findById(testTransaction.getId());
    }



}