package com.example.bookingstore.dto.responses;

import com.example.bookingstore.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private Long customerId;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;

    public OrderResponse(Long orderId, Long customerId, BigDecimal totalPrice, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }
}
