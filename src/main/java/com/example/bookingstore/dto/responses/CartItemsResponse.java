package com.example.bookingstore.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
public class CartItemsResponse {

    private Set<BookDetail> bookDetails;
    private Double totalPrice;
}

