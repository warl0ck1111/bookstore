package com.example.bookingstore.mapper;

import com.example.bookingstore.dto.BookResponseDto;
import com.example.bookingstore.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "authorId", source = "book.author.id")
    BookResponseDto bookToBookResponseDto(Book book);

}
