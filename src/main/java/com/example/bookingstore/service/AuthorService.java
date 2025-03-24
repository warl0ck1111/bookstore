package com.example.bookingstore.service;

import com.example.bookingstore.dto.AuthorDto;
import com.example.bookingstore.entity.Author;

public interface AuthorService {

    Author createAuthor(AuthorDto author);

    void updateAuthorsName(Long authorId, String author);

    Author findById(Long authorId);

    void deleteAuthor(Long authorId);

}
