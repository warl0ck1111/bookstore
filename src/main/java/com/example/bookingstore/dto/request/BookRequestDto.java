package com.example.bookingstore.dto.request;


import com.example.bookingstore.enums.Genre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BookRequestDto(

        @NotBlank(message = "title can not be null or empty")
        @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only numbers and letters")
        String title,

        @NotNull(message = "Invalid genre")
        Genre genre,

        @NotBlank(message = "isbn can not be null or empty")
        @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dash")
        String isbn,

        @Min(value = 1, message = "invalid author Id")
        @NotNull(message = "Invalid author Id")
        Long authorId,

        @NotNull(message = "Invalid year Of Publication")
        @Min(value = 1, message = "Invalid year of publication")
        Integer yearOfPublication,


        @NotNull(message = "Invalid price")
        @Min(value = 1, message = "Invalid price")
        Double price,

        @NotNull(message = "Invalid stock amount")
        @Min(value = 1, message = "Invalid stock amount")
        Integer stock
) {
}
