package com.example.bookingstore.controller;


import com.example.bookingstore.dto.BookRequestDto;
import com.example.bookingstore.dto.BookResponseDto;
import com.example.bookingstore.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = "title") String sortField,
            @RequestParam(name = "sortDirection",required = false, defaultValue = "ASC") Sort.Direction sortDirection){
        return ResponseEntity.ok(bookService.getAllBooks(keyword, page, pageSize, sortField, sortDirection));
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto bookResponseDto = bookService.addBook(bookRequestDto);
        return ResponseEntity.ok(bookResponseDto);
    }

    @PutMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookResponseDto> updateBookAuthor(@PathVariable Long bookId,@PathVariable Long authorId) {
        bookService.updateBookAuthor(bookId, authorId);
        return ResponseEntity.noContent().build();
    }




}
