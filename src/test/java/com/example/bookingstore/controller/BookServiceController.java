package com.example.bookingstore.controller;


import com.example.bookingstore.dto.request.BookRequestDto;
import com.example.bookingstore.dto.responses.BookResponseDto;
import com.example.bookingstore.enums.Genre;
import com.example.bookingstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.HttpStatus.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private BookResponseDto bookResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        bookResponseDto = new BookResponseDto(1L, "Harry Potter", Genre.FICTION, "978-0747532699", 1L, 1997, 1500.00, 10);
    }

    @Test
    void shouldGetBooks() {
        // Given
        Page<BookResponseDto> bookPage = new PageImpl<>(List.of(bookResponseDto));
        when(bookService.getAllBooks(anyString(), anyInt(), anyInt(), anyString(), any(Sort.Direction.class)))
                .thenReturn(bookPage);

        // When
        ResponseEntity<Page<BookResponseDto>> response = bookController.getBooks("Harry", 0, 10, "title", Sort.Direction.ASC);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldCreateBook() throws Exception {
        // Given
        BookRequestDto bookRequestDto = new BookRequestDto("Harry Potter", Genre.FICTION, "978-0747532699",
                1L, 1997, 1500.00, 10);

        when(bookService.addBook(any(BookRequestDto.class))).thenReturn(bookResponseDto);

        // When & Then - Testing API response using MockMvc
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Harry Potter",
                                    "genre": "FICTION",
                                    "isbn": "978-0747532699",
                                    "authorId": 1,
                                    "yearOfPublication": 1997,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.isbn").value("978-0747532699"));

        verify(bookService, times(1)).addBook(any(BookRequestDto.class));
    }

    @Test
    void shouldUpdateBookAuthor() throws Exception {
        // Given
        doNothing().when(bookService).updateBookAuthor(1L, 2L);

        // When & Then
        mockMvc.perform(put("/api/v1/books/1/author/2"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).updateBookAuthor(1L, 2L);
    }
}