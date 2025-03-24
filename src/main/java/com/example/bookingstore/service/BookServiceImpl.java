package com.example.bookingstore.service;

import com.example.bookingstore.dto.BookRequestDto;
import com.example.bookingstore.dto.BookResponseDto;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.enums.Genre;
import com.example.bookingstore.exceptions.BookServiceException;
import com.example.bookingstore.exceptions.ResourceNotFoundException;
import com.example.bookingstore.mapper.BookingMapper;
import com.example.bookingstore.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookingMapper bookingMapper;
    private final AuthorService authorService;

    @Override
    public BookResponseDto addBook(BookRequestDto bookRequestDto) {
        log.info("addBook/BookRequestDto: {}", bookRequestDto);

        if (bookRepository.existsByIsbnIgnoreCase(bookRequestDto.isbn()))throw new BookServiceException("no two books can have the same isbn");

        Book book = new Book();
        book.setTitle(bookRequestDto.title());
        book.setGenre(bookRequestDto.genre());
        book.setPrice(bookRequestDto.price());
        book.setStock(bookRequestDto.stock());
        book.setIsbn(bookRequestDto.isbn());
        book.setYearOfPublication(bookRequestDto.yearOfPublication());
        book.setAuthor(authorService.findById(bookRequestDto.authorId()));
        Book savedBook = bookRepository.save(book);
        return bookingMapper.bookToBookResponseDto(savedBook);
    }

    @Override
    public void updateBookTitle(Long bookId, String title) {
        log.info("updateBookTitle/BookId: {} title:{}", bookId,title);
        bookRepository.updateBookTitle(bookId, title);
    }

    @Override
    public void updateBookAuthor(Long bookId, Long authorId) {
        log.info("updateBookTitle/BookId: {} authorId:{}", bookId,authorId);
        bookRepository.updateBookAuthor(authorId, bookId);
    }

    @Override
    public void updateBookGenre(Long bookId, Genre genre) {
        log.info("updateBookTitle/BookId: {} genre:{}", bookId,genre);
        bookRepository.updateBookGenre(bookId, genre);
    }

    @Override
    public void updateBookIsbn(Long bookId, String isbn) {
        log.info("updateBookTitle/BookId: {} isbn:{}", bookId,isbn);
        bookRepository.updateBookIsbn(bookId, isbn);
    }

    @Override
    public void updateBookYearOfPublication(Long bookId, int yearOfPublication) {
        log.info("updateBookYearOfPublication/BookId: {} yearOfPublication:{}", bookId,yearOfPublication);
        bookRepository.updateBookYearOfPublication(bookId, yearOfPublication);
    }


    @Override
    public void updateBookPrice(Long bookId, Double price) {
        log.info("updateBookPrice/BookId: {} price:{}", bookId,price);
        bookRepository.updateBookPrice(bookId, price);
    }


    @Override
    public BookResponseDto getBookById(Long bookId) {
        log.info("getBookById/BookId: {}", bookId);
        Book book = findBookById(bookId);
        return bookingMapper.bookToBookResponseDto(book);
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findByIdAndDeletedFalse(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id:"+bookId + " not found"));
    }

    @Override
    public Page<BookResponseDto> getAllBooks(String keyword, int page, int pageSize, String sortField, Sort.Direction sortDirection) {
        log.info("getAllBooks/keyword:{} page:{} pageSize:{}", keyword,page,pageSize);
        Sort sort = Sort.by(sortDirection, sortField);
        if (StringUtils.isNotBlank(keyword)) {
            return bookRepository.searchBooks(keyword, PageRequest.of(page, pageSize, sort));
        } else {
            return bookRepository.findAllBooks(PageRequest.of(page, pageSize, sort));
        }

    }

    @Override
    public void deleteBook(Long bookId) {
        log.info("deleteBook/BookId: {}", bookId);
        bookRepository.markBookAsDeleted(bookId);
    }

    @Override
    public void restoreBook(Long bookId) {
        log.info("restoreBook/BookId: {}", bookId);
        bookRepository.findByIdAndDeletedFalse(bookId).ifPresent(book -> {
            book.setDeleted(false);
            bookRepository.save(book);
        });
    }

}