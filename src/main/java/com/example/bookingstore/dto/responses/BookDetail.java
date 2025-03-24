package com.example.bookingstore.dto.responses;

import com.example.bookingstore.dto.BookResponseDto;
import lombok.Data;

@Data
public class BookDetail{
    private BookResponseDto book;
    private int quantity;
}
