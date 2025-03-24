package com.example.bookingstore.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerValidationTest {



    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "genre": "FICTION",
                                    "isbn": "978-0747532699",
                                    "authorId": 1,
                                    "yearOfPublication": 1997,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
  @Test
    void shouldReturnBadRequestWhenTitleContainsSpecialCharacters() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {   "title": "7887*&*&GHgvgh",
                                    "genre": "FICTION",
                                    "isbn": "978-0747532699",
                                    "authorId": 1,
                                    "yearOfPublication": 1997,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldReturnBadRequestWhenIsbnContainsLetters() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {   "title": "title",
                                    "genre": "FICTION",
                                    "isbn": "978-074HJ7532699",
                                    "authorId": 1,
                                    "yearOfPublication": 1997,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturnBadRequestWhenIsbnIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Harry Potter",
                                    "genre": "FICTION",
                                    "isbn": "INVALID-ISBN!",
                                    "authorId": 1,
                                    "yearOfPublication": 1997,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAuthorIdIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Harry Potter",
                                    "genre": "FICTION",
                                    "isbn": "978-0747532699",
                                    "authorId": 0,
                                    "yearOfPublication": 1997,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenYearOfPublicationIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Harry Potter",
                                    "genre": "FICTION",
                                    "isbn": "978-0747532699",
                                    "authorId": 1,
                                    "yearOfPublication": 0,
                                    "price": 1500.00,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Harry Potter",
                                    "genre": "FICTION",
                                    "isbn": "978-0747532699",
                                    "authorId": 1,
                                    "yearOfPublication": 1997,
                                    "price": 0,
                                    "stock": 10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenStockIsInvalid() throws Exception {
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
                                    "stock": 0
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}