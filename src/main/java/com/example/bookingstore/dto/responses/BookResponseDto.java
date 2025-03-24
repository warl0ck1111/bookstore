package com.example.bookingstore.dto.responses;

import com.example.bookingstore.enums.Genre;

public record BookResponseDto(
        Long id,
        String title,
        Genre genre,
        String isbn,
        Long authorId,
        Integer yearOfPublication,
        Double price,
        Integer stock

) {}