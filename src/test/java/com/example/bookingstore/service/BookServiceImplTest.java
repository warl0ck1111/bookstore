package com.example.bookingstore.service;

import com.example.bookingstore.dto.request.BookRequestDto;
import com.example.bookingstore.dto.responses.BookResponseDto;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.enums.Genre;
import com.example.bookingstore.exceptions.BookServiceException;
import com.example.bookingstore.exceptions.ResourceNotFoundException;
import com.example.bookingstore.mapper.BookingMapper;
import com.example.bookingstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book savedBook;
    private Author savedAuthor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setName("J.K. Rowling");

        savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Harry Potter");
        savedBook.setGenre(Genre.FICTION);
        savedBook.setIsbn("978-0747532699");
        savedBook.setAuthor(savedAuthor);
        savedBook.setYearOfPublication(1997);
        savedBook.setStock(10);
        savedBook.setPrice(1500.00);
        savedBook.setDeleted(false);
    }

    @Test
    void shouldAddBookSuccessfully() {
        // Given
        BookRequestDto bookRequestDto = new BookRequestDto("Harry Potter", Genre.FICTION, "978-0747532699",
                1L, 1997, 1500.00, 10);

        when(authorService.findById(1L)).thenReturn(savedAuthor);
        when(bookRepository.existsByIsbnIgnoreCase(bookRequestDto.isbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        when(bookingMapper.bookToBookResponseDto(savedBook)).thenReturn(new BookResponseDto(1L, "Harry Potter", Genre.FICTION, "978-0747532699", 1L, 1997, 1500.00, 10));

        // When
        BookResponseDto response = bookService.addBook(bookRequestDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("Harry Potter");
        assertThat(response.isbn()).isEqualTo("978-0747532699");

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateISBN() {
        // Given
        BookRequestDto bookRequestDto = new BookRequestDto("Harry Potter", Genre.FICTION, "978-0747532699",
                1L, 1997, 1500.00, 10);

        when(bookRepository.existsByIsbnIgnoreCase(bookRequestDto.isbn())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> bookService.addBook(bookRequestDto))
                .isInstanceOf(BookServiceException.class)
                .hasMessage("no two books can have the same isbn");

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldUpdateBookTitle() {
        // When
        bookService.updateBookTitle(1L, "New Title");

        // Then
        verify(bookRepository, times(1)).updateBookTitle(1L, "New Title");
    }

    @Test
    void shouldUpdateBookAuthor() {
        // When
        bookService.updateBookAuthor(1L, 2L);

        // Then
        verify(bookRepository, times(1)).updateBookAuthor(2L, 1L);
    }

    @Test
    void shouldUpdateBookGenre() {
        // When
        bookService.updateBookGenre(1L, Genre.THRILLER);

        // Then
        verify(bookRepository, times(1)).updateBookGenre(1L, Genre.THRILLER);
    }

    @Test
    void shouldUpdateBookIsbn() {
        // When
        bookService.updateBookIsbn(1L, "123-456-789");

        // Then
        verify(bookRepository, times(1)).updateBookIsbn(1L, "123-456-789");
    }

    @Test
    void shouldUpdateBookPrice() {
        // When
        bookService.updateBookPrice(1L, 2000.00);

        // Then
        verify(bookRepository, times(1)).updateBookPrice(1L, 2000.00);
    }

    @Test
    void shouldFindBookById() {
        // Given
        when(bookRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(savedBook));
        when(bookingMapper.bookToBookResponseDto(savedBook)).thenReturn(new BookResponseDto(1L, "Harry Potter", Genre.FICTION, "978-0747532699", 1L, 1997, 1500.00, 10));

        // When
        BookResponseDto foundBook = bookService.getBookById(1L);

        // Then
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.title()).isEqualTo("Harry Potter");

        verify(bookRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        // Given
        when(bookRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookService.getBookById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book with id:1 not found");

        verify(bookRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void shouldDeleteBook() {
        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository, times(1)).markBookAsDeleted(1L);
    }

    @Test
    void shouldRestoreBook() {
        // Given
        savedBook.setDeleted(true);
        when(bookRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(savedBook));

        // When
        bookService.restoreBook(1L);

        // Then
        verify(bookRepository, times(1)).save(savedBook);
        assertThat(savedBook.getDeleted()).isFalse();
    }
}