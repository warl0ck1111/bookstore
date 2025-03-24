package com.example.bookingstore.repository;

import com.example.bookingstore.dto.responses.CartItemProjection;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.entity.Cart;
import com.example.bookingstore.entity.CartItem;
import com.example.bookingstore.entity.User;
import com.example.bookingstore.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    private Cart savedCart;

    public static final String TEST_BOOK_TITLE = "Harry Potter and the Philosophers Stone";


    @BeforeEach
    void setUp() {
        // Given - Creating and saving a user
        User user = new User();
        user.setUsername("test_user");
        user = userRepository.save(user);

        // Given - Creating and saving a cart for the user
        Cart cart = new Cart();
        cart.setUser(user);
        savedCart = cartRepository.save(cart);

        // Given - Creating and saving an author
        Author author = new Author();
        author.setName("J.K. Rowling");
        author = authorRepository.save(author);

        // Given - Creating and saving a book
        Book book = new Book();
        book.setTitle(TEST_BOOK_TITLE);
        book.setGenre(Genre.FICTION);
        book.setIsbn("978-0747532699");
        book.setAuthor(author);
        book.setYearOfPublication(1997);
        book.setStock(10);
        book.setPrice(1500.00);
        book = bookRepository.save(book);

        // Given - Creating and saving a cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(savedCart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);
        cartItemRepository.save(cartItem);
    }

    @Test
    @Rollback
    void shouldFindCartItemsByCartId() {
        // When - Retrieving cart items
        List<CartItemProjection> cartItems = cartItemRepository.findByCartId(savedCart.getId());

        // Then - Validate cart items exist
        assertThat(cartItems).isNotEmpty();
        assertThat(cartItems.getFirst().getBookTitle()).isEqualTo(TEST_BOOK_TITLE);
        assertThat(cartItems.getFirst().getQuantity()).isEqualTo(2);
    }

    @Test
    @Rollback
    void shouldDeleteCartItemsByCart() {
        // When - Deleting cart items by cart
        cartItemRepository.deleteByCart(savedCart);
        List<CartItemProjection> cartItems = cartItemRepository.findByCartId(savedCart.getId());

        // Then - Validate cart items are deleted
        assertThat(cartItems).isEmpty();
    }
}