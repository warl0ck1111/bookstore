package com.example.bookingstore.repository;

import com.example.bookingstore.dto.responses.BookResponseDto;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.enums.Genre;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    public static final String TEST_BOOK_TITLE = "Harry Potter and the Philosophers Stone";
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book savedBook;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Given - Creating and saving an author
        Author author = new Author();
        author.setName("J.K. Rowling");
        Author savedAuthor = authorRepository.save(author);

        // Given - Creating and saving a book
        Book book = new Book();
        book.setTitle(TEST_BOOK_TITLE);
        book.setGenre(Genre.FICTION);
        book.setIsbn("978-0747532699");
        book.setAuthor(savedAuthor);
        book.setYearOfPublication(1997);
        book.setStock(10);
        book.setPrice(1500.00);
        book.setDeleted(false);

        savedBook = bookRepository.save(book);
    }

    @Test
    @Rollback
    void shouldFindBookByIdAndDeletedFalse() {
        // When - Fetching book by ID
        Optional<Book> foundBook = bookRepository.findByIdAndDeletedFalse(savedBook.getId());

        // Then - Validate book exists
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo(TEST_BOOK_TITLE);
    }

    @Test
    @Rollback
    void shouldUpdateBookPrice() {
        // When - Updating book price
        bookRepository.updateBookPrice(savedBook.getId(), 2000.00);
        entityManager.flush(); //synchronize the persistence context with the database
        entityManager.clear(); //is called to clear the first-level cache, ensuring that the subsequent retrieval of the entity reflects the updated state from the database.

        Optional<Book> updatedBook = bookRepository.findById(savedBook.getId());

        // Then - Validate price update
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getPrice()).isEqualTo(2000.00);
    }

    @Test
    @Rollback
    void shouldUpdateBookTitle() {
        // When - Updating book title
        bookRepository.updateBookTitle(savedBook.getId(), "Harry Potter and the Chamber of Secrets");
        entityManager.flush(); //synchronize the persistence context with the database
        entityManager.clear(); //is called to clear the first-level cache, ensuring that the subsequent retrieval of the entity reflects the updated state from the database.

        Optional<Book> updatedBook = bookRepository.findById(savedBook.getId());

        // Then - Validate title update
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo("Harry Potter and the Chamber of Secrets");
    }

    @Test
    @Rollback
    void shouldUpdateBookGenre() {
        // When - Updating book genre
        bookRepository.updateBookGenre(savedBook.getId(), Genre.THRILLER);
        entityManager.flush(); //synchronize the persistence context with the database
        entityManager.clear(); //is called to clear the first-level cache, ensuring that the subsequent retrieval of the entity reflects the updated state from the database.

        Optional<Book> updatedBook = bookRepository.findById(savedBook.getId());

        // Then - Validate genre update
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getGenre()).isEqualTo(Genre.THRILLER);
    }

    @Test
    @Rollback
    void shouldUpdateBookAuthor() {
        // Given - Creating a new author
        Author newAuthor = new Author();
        newAuthor.setName("George R.R. Martin");
        Author savedNewAuthor = authorRepository.save(newAuthor);

        // When - Updating book's author
        bookRepository.updateBookAuthor(savedNewAuthor.getId(), savedBook.getId());
        entityManager.flush(); //synchronize the persistence context with the database
        entityManager.clear(); //is called to clear the first-level cache, ensuring that the subsequent retrieval of the entity reflects the updated state from the database.


        Optional<Book> updatedBook = bookRepository.findById(savedBook.getId());


        // Then - Validate author update
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getAuthor().getId()).isEqualTo(savedNewAuthor.getId());
    }

    @Test
    @Rollback
    void shouldMarkBookAsDeleted() {
        // When - Marking book as deleted
        bookRepository.markBookAsDeleted(savedBook.getId());
        Optional<Book> deletedBook = bookRepository.findByIdAndDeletedFalse(savedBook.getId());

        entityManager.flush(); //synchronize the persistence context with the database
        entityManager.clear(); //is called to clear the first-level cache, ensuring that the subsequent retrieval of the entity reflects the updated state from the database.

        // Then - Validate book is not retrievable
        assertThat(deletedBook).isEmpty();
    }

    @Test
    @Rollback
    void shouldSearchBooksByKeyword() {
        // When - Searching books by title keyword
        Page<BookResponseDto> result = bookRepository.searchBooks("Harry", PageRequest.of(0, 5));

        // Then - Validate book is found
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().getFirst().title()).containsIgnoringCase("Harry");
    }
}