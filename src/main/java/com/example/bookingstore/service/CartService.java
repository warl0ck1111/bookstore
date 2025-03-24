package com.example.bookingstore.service;

import com.example.bookingstore.dto.responses.CartItemsResponse;
import com.example.bookingstore.entity.Cart;

public interface CartService {
    Cart getCartByUserId(Long userId);

    void addBookToCart(Long userId, Long bookId, int quantity);

    void removeBookFromCart(Long userId, Long bookId, int quantity);

    CartItemsResponse getBooksInCart(Long userId);

    void clearCart(Long userId);
}
