package com.example.bookingstore.service;

import com.example.bookingstore.dto.OrderResponse;
import com.example.bookingstore.entity.*;
import com.example.bookingstore.enums.OrderStatus;
import com.example.bookingstore.enums.PaymentMethod;
import com.example.bookingstore.exceptions.InsufficientStockException;
import com.example.bookingstore.exceptions.PaymentServiceException;
import com.example.bookingstore.exceptions.ResourceNotFoundException;
import com.example.bookingstore.repository.BookRepository;
import com.example.bookingstore.repository.CartItemRepository;
import com.example.bookingstore.repository.CartRepository;
import com.example.bookingstore.repository.OrderRepository;
import com.example.bookingstore.transaction.TransactionService;
import com.example.bookingstore.transaction.models.PaymentChannel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final TransactionService transactionService;

    @Transactional
    @Override
    public OrderResponse checkout(Long userId, PaymentMethod paymentMethod, PaymentChannel paymentChannel) {
        log.info("checkout/userId:{} paymentMethod:{}", userId, paymentMethod);
        // Find user's cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user"));

        Set<CartItem> cartItems = cart.getCartItems();
        log.info("checkout/cartItems size:{}", cartItems.size());
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        // Create order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();

            // Check stock availability
            if (book.getStock() < quantity) {
                throw new InsufficientStockException("Not enough stock for book: " + book.getTitle() + " with id "+book.getId()+ ". Requested " + cartItem.getQuantity() + " but only got " + book.getStock() + " left in stock");
            }

            // Deduct stock
            book.setStock(book.getStock() - quantity);
            bookRepository.save(book);

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(BigDecimal.valueOf(book.getPrice() * quantity));
            order.getOrderItems().add(orderItem);

            totalPrice = totalPrice.add((orderItem.getPrice()));
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        // Simulate payment processing


        boolean paymentSuccessful = transactionService.processOrderPayment(order, paymentMethod, paymentChannel);

        if (paymentSuccessful) {
            savedOrder.setStatus(OrderStatus.PAID);
        } else {
            log.error("Payment failed");
            throw new PaymentServiceException("There was an error processing payment, please try again");
        }

        orderRepository.save(savedOrder);

        // Clear cart
        cartItemRepository.deleteByCart(cart);

        return new OrderResponse(order.getId(),savedOrder.getUser().getId(),totalPrice, savedOrder.getStatus());
    }

}