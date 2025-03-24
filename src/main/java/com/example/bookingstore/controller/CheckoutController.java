package com.example.bookingstore.controller;

import com.example.bookingstore.dto.responses.OrderResponse;
import com.example.bookingstore.dto.responses.ApiSuccessResponse;
import com.example.bookingstore.enums.PaymentMethod;
import com.example.bookingstore.service.CheckoutService;
import com.example.bookingstore.transaction.models.PaymentChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiSuccessResponse> checkout(@PathVariable Long userId, @RequestParam PaymentMethod paymentMethod, @RequestParam PaymentChannel paymentChannel) {
        OrderResponse order = checkoutService.checkout(userId, paymentMethod, paymentChannel);
        return ResponseEntity.ok(new ApiSuccessResponse(order,""));
    }

}