package com.example.bookingstore.service;


import com.example.bookingstore.dto.BookResponseDto;
import com.example.bookingstore.dto.responses.BookDetail;
import com.example.bookingstore.dto.responses.CartItemProjection;
import com.example.bookingstore.dto.responses.CartItemsResponse;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.entity.Cart;
import com.example.bookingstore.entity.User;
import com.example.bookingstore.exceptions.CartServiceException;
import com.example.bookingstore.exceptions.ResourceNotFoundException;
import com.example.bookingstore.repository.BookRepository;
import com.example.bookingstore.repository.CartRepository;
import com.example.bookingstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.bookingstore.entity.CartItem;
import com.example.bookingstore.repository.CartItemRepository;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public void addBookToCart(Long userId, Long bookId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Book book = getBookById(bookId);

        if (book.getStock() == 0) throw new CartServiceException("Book with id:" + bookId + " is out of stock");

        if (book.getStock() < quantity)
            throw new CartServiceException("only " + book.getStock() + "Book with id:" + bookId + " is over stock");

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();//todo

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(quantity);
        } else {
            CartItem cartItem = new CartItem(cart, book, quantity);
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
    }


    @Override
    public void removeBookFromCart(Long userId, Long bookId, int quantity) {
        Cart cart = getCartByUserId(userId);

        cart.getCartItems().removeIf(item -> {
            if (item.getBook().getId().equals(bookId)) {
                item.decreaseQuantity(quantity);
                return item.getQuantity() == 0;
            }
            return false;
        });

        cartRepository.save(cart);
    }

    @Override
    public CartItemsResponse getBooksInCart(Long cartId) {
        log.info("getting books in cart:{}", cartId);
        Set<BookDetail> bookDetails = new HashSet<>();
        List<CartItemProjection> cartItemProjections = cartItemRepository.findByCartId(cartId);
        double totalPrice = 0;
        for (CartItemProjection cartItemProjection : cartItemProjections) {
            BookDetail bookDetail =  new BookDetail();
            bookDetail.setBook(new BookResponseDto(cartItemProjection.getBookId(), cartItemProjection.getBookTitle(), cartItemProjection.getGenre(), cartItemProjection.getIsbn(), cartItemProjection.getAuthorId(), cartItemProjection.getYearOfPublication(),cartItemProjection.getPrice(), cartItemProjection.getStock()));
            bookDetail.setQuantity(cartItemProjection.getQuantity());
            bookDetails.add(bookDetail);

            //add to total price
            totalPrice += cartItemProjection.getPrice() * cartItemProjection.getQuantity();
        }
        return CartItemsResponse.builder().bookDetails(bookDetails).totalPrice(totalPrice).build();
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findByIdAndDeletedFalse(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with id:" + bookId + " not found"));
    }
}