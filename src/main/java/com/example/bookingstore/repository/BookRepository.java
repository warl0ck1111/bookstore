package com.example.bookingstore.repository;

import com.example.bookingstore.dto.BookResponseDto;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b where b.id=:bookId and b.deleted= false")
    Optional<Book> findByIdAndDeletedFalse(@Param("bookId") Long bookId);

    @Query(value = """
        SELECT new com.example.bookingstore.dto.BookResponseDto(b.id, b.title, b.genre,b.isbn,b.author.id, b.yearOfPublication, b.price, b.stock) from Book b where b.deleted = false
    """)
    Page<BookResponseDto> findAllBooks(Pageable pageable);

    @Modifying
    @Query("update Book b set b.price = :price where b.id = :id")
    void updateBookPrice(@Param("id") Long id, @Param("price") Double price);

    @Query("""
        SELECT new com.example.bookingstore.dto.BookResponseDto(b.id, b.title, b.genre,b.isbn,b.author.id, b.yearOfPublication,b.price, b.stock)  FROM Book b
        WHERE b.deleted = false
        AND (
            LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(b.author.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR CAST(b.yearOfPublication AS string) LIKE CONCAT('%', :keyword, '%')
            OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<BookResponseDto> searchBooks(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Book b set b.yearOfPublication= :yearOfPublication where b.id=:bookId")
    void updateBookYearOfPublication(@Param("bookId") Long bookId, @Param("yearOfPublication") int yearOfPublication);

    @Transactional
    @Modifying
    @Query("update Book b set b.title = :title where b.id = :bookId")
    void updateBookTitle(@Param("bookId")Long bookId, @Param("title")String title);

    @Transactional
    @Modifying
    @Query("update Book b set b.isbn = :isbn where b.id = :id")
    void updateBookIsbn( @Param("id") Long id, @Param("isbn") String isbn);

    @Transactional
    @Modifying
    @Query("update Book b set b.deleted = true where b.id = :id")
    void markBookAsDeleted(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Book b set b.genre = :genre where b.id = :id")
    void updateBookGenre( @Param("id") Long id, @Param("genre") Genre genre);


    boolean existsByIsbnIgnoreCase(String isbn);

    @Transactional
    @Modifying
    @Query("update Book b set b.author.id = :author where b.id = :id")
    void updateBookAuthor(@Param("author") Long author, @Param("id") Long id);
}
