package com.example.bookingstore.transaction;

import com.example.bookingstore.transaction.models.Transaction;
import com.example.bookingstore.transaction.models.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Modifying
    @Query(value = "UPDATE Transaction t set t.transactionStatus = :transactionStatus, t.paymentReference = :paymentReference where t.id=:transactionId")
    void updateTransactionStatusAndPaymentReferenceById(@Param("transactionId") String transactionId, @Param("paymentReference") String paymentReference, @Param("transactionStatus") TransactionStatus transactionStatus);

    List<Transaction> findByUserId(Long userId);

}
