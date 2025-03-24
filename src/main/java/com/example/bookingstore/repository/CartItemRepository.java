package com.example.bookingstore.repository;

import com.example.bookingstore.dto.responses.CartItemProjection;
import com.example.bookingstore.entity.Cart;
import com.example.bookingstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select c.id as id, c.book.title as bookTitle, c.quantity as quantity from CartItem c where c.cart.id = :id")
    List<CartItemProjection> getBooksInCart(@Param("id") Long id);


    @Query("""
            select new com.example.bookingstore.dto.responses.CartItemProjection(
             c.id,  c.book.id , c.book.title,
            c.quantity , c.book.genre , c.book.isbn,
            c.book.yearOfPublication ,
            c.book.author.id , c.book.author.name , c.book.price, c.book.stock )
            from CartItem c where c.cart.id = :cartId group by c.id,c.book.yearOfPublication,c.book.title,c.quantity, c.book.genre, c.book.isbn, c.book.author.id, c.book.author.name , c.book.price, c.book.stock
            """)
    List<CartItemProjection> findByCartId(@Param("cartId") Long cartId);

    @Query("select c from CartItem c where c.cart = :cart")
    List<CartItem> findByCart(@Param("cart") Cart cart);

}
