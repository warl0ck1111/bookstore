package com.example.bookingstore.dto.responses;

import com.example.bookingstore.enums.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemProjection {

    private int quantity;
    private Double price;
    private Long bookId;
    private String bookTitle;
    private Long authorId;
    private String authorName;
    private String isbn;
    private Integer yearOfPublication;
    private Long cartItemId;
    private Long cartId;
    private Genre genre;
    private Integer stock;

    public CartItemProjection(Long cartItemId, Long bookId, String bookTitle, Integer quantity, Genre genre, String isbn, Integer yearOfPublication, Long authorId, String authorName, Double price,Integer stock) {
        this.cartItemId = cartItemId;
        this.bookId = bookId;
        this.authorName = authorName;
        this.stock = stock;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.genre = genre;
        this.isbn = isbn;
        this.yearOfPublication = yearOfPublication;
        this.authorId = authorId;
        this.price = price;
    }


//    Long getCartId();
//    String getBookTitle();
//    Integer getQuantity();
//    Long getBookId();
//    Long getAuthorId();
//    Genre getBookGenre();
//    String getBookIsbn();
//    Double getBookPrice();
//    Integer getYearOfPublication();

}
