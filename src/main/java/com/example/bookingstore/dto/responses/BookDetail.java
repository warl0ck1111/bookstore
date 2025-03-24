package com.example.bookingstore.dto.responses;

import lombok.Data;

@Data
public class BookDetail{
    private BookResponseDto book;
    private int quantity;
}
