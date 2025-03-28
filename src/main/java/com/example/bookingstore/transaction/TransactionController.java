package com.example.bookingstore.transaction;


import com.example.bookingstore.dto.responses.ApiSuccessResponse;
import com.example.bookingstore.transaction.models.TransactionResponse;
import com.example.bookingstore.transaction.models.VerifyTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{transactionId}/verify")
    public ResponseEntity<ApiSuccessResponse> checkTransaction(@PathVariable String transactionId) {
        VerifyTransactionResponse transactionResponse = transactionService.verifyTransaction(transactionId);
        return ResponseEntity.ok(new ApiSuccessResponse(transactionResponse,""));
    }


    @GetMapping("")
    public ResponseEntity<ApiSuccessResponse> getTransactionHistory(@RequestParam Long userId) {
        List<TransactionResponse> transactionResponse = transactionService.getPurchaseHistory(userId);
        return ResponseEntity.ok(new ApiSuccessResponse(transactionResponse,""));
    }
}
