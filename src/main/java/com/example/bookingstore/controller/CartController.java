package com.example.bookingstore.controller;

import com.example.bookingstore.dto.responses.ApiSuccessResponse;
import com.example.bookingstore.dto.responses.CartItemsResponse;
import com.example.bookingstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/{userId}/add/{bookId}/{quantity}")
    public ResponseEntity<ApiSuccessResponse> addBookToCart(@PathVariable Long userId, @PathVariable Long bookId, @PathVariable int quantity) {
        cartService.addBookToCart(userId, bookId, quantity);
        return ResponseEntity.ok(new ApiSuccessResponse("Book added to cart successfully."));
    }

    @PutMapping("/{userId}/remove/{bookId}/{quantity}")
    public ResponseEntity<ApiSuccessResponse> removeBookFromCart(@PathVariable Long userId, @PathVariable Long bookId, @PathVariable int quantity) {
        cartService.removeBookFromCart(userId, bookId, quantity);
        return ResponseEntity.ok(new ApiSuccessResponse("Book quantity updated."));
    }



    @GetMapping("/{userId}")
    public ResponseEntity<ApiSuccessResponse> getBooksInCart(@PathVariable Long userId) {
        CartItemsResponse booksInCart = cartService.getBooksInCart(userId);
        return ResponseEntity.ok(new ApiSuccessResponse(booksInCart,""));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiSuccessResponse> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(new ApiSuccessResponse("cart cleared successfully."));
    }


}
