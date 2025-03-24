package com.example.bookingstore.service;


import com.example.bookingstore.dto.request.BookRequestDto;
import com.example.bookingstore.dto.responses.BookResponseDto;
import com.example.bookingstore.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface BookService {

    BookResponseDto addBook(BookRequestDto bookRequestDto);

    void updateBookTitle(Long bookId, String title);
    void updateBookAuthor(Long bookId, Long AuthorId);
    void updateBookGenre(Long bookId, Genre genre);
    void updateBookIsbn(Long bookId, String isbn);
    void updateBookYearOfPublication(Long bookId, int yearOfPublication);

    void updateBookPrice(Long bookId, Double price);

    BookResponseDto getBookById(Long bookId);

    Page<BookResponseDto> getAllBooks(String search, int page, int pageSize, String sortField, Sort.Direction sortDirection);

    void deleteBook(Long bookId);

    void restoreBook(Long bookId);

//    boolean isBookAvailable(Long bookId); // Check if a book is in stock
}
