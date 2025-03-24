package com.example.bookingstore.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

//    @Mapping(target = "book", source = ".", qualifiedByName = "mapToBookResponseDto")
//    CartItemsResponse cartItemProjectionToCartItemsResponse(CartItemProjection cartItemProjection);
//
//    @Named("mapToBookResponseDto")
//    default BookResponseDto mapToBookResponseDto(CartItemProjection projection) {
//        return new BookResponseDto(
//                projection.getBookId(),
//                projection.getBookTitle(),
//                projection.getBookGenre(),
//                projection.getBookIsbn(),
//                projection.getAuthorId(),
//                projection.getYearOfPublication()
//        );
//    }
}