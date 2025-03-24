package com.example.bookingstore.service;

import com.example.bookingstore.dto.responses.OrderResponse;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    private Cart testCart;
    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test user
        User testUser = new User();
        testUser.setId(1L);

        // Create test book
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setStock(10);
        testBook.setPrice(100.00);

        // Create test cart item
        CartItem testCartItem = new CartItem();
        testCartItem.setCart(testCart);
        testCartItem.setBook(testBook);
        testCartItem.setQuantity(2);

        // Create test cart with cart items
        testCart = new Cart();
        testCart.setUser(testUser);
        testCart.setCartItems(Set.of(testCartItem));
    }

    @Test
    void shouldCheckoutSuccessfully() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionService.processOrderPayment(any(Order.class), any(PaymentMethod.class), any(PaymentChannel.class)))
                .thenReturn(true);

        // When
        OrderResponse response = checkoutService.checkout(1L, PaymentMethod.TRANSFER, PaymentChannel.MOCK_PAYMENT_CHANNEL_1);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(response.getTotalPrice()).isEqualTo(BigDecimal.valueOf(200.00));

        verify(orderRepository, times(2)).save(any(Order.class)); // Initial save & final save after payment
        verify(cartItemRepository, times(1)).deleteByCart(testCart);
    }

    @Test
    void shouldThrowExceptionWhenCartNotFound() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> checkoutService.checkout(1L, PaymentMethod.TRANSFER, PaymentChannel.MOCK_PAYMENT_CHANNEL_1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cart not found for user");

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {
        // Given
        testCart.setCartItems(new HashSet<>()); // Empty cart
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        // Then
        assertThatThrownBy(() -> checkoutService.checkout(1L, PaymentMethod.TRANSFER, PaymentChannel.MOCK_PAYMENT_CHANNEL_2))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart is empty");

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        // Given
        testBook.setStock(1); // Not enough stock
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        // Then
        assertThatThrownBy(() -> checkoutService.checkout(1L, PaymentMethod.TRANSFER, PaymentChannel.MOCK_PAYMENT_CHANNEL_2))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Not enough stock for book: Test Book");

        verify(bookRepository, never()).save(any(Book.class)); // Stock update should never happen
    }

    @Test
    void shouldThrowExceptionWhenPaymentFails() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionService.processOrderPayment(any(Order.class), any(PaymentMethod.class), any(PaymentChannel.class)))
                .thenReturn(false);

        // Then
        assertThatThrownBy(() -> checkoutService.checkout(1L, PaymentMethod.TRANSFER, PaymentChannel.MOCK_PAYMENT_CHANNEL_2))
                .isInstanceOf(PaymentServiceException.class)
                .hasMessage("There was an error processing payment, please try again");

        verify(orderRepository, times(1)).save(any(Order.class)); // Should not update order status to PAID
    }
}