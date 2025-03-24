package com.example.bookingstore.service;

import com.example.bookingstore.dto.responses.OrderResponse;
import com.example.bookingstore.enums.PaymentMethod;
import com.example.bookingstore.transaction.models.PaymentChannel;

public interface CheckoutService {

    OrderResponse checkout(Long userId, PaymentMethod paymentMethod, PaymentChannel paymentChannel);
}
