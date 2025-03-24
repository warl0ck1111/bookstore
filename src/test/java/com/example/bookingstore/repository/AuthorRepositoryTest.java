package com.example.bookingstore.repository;

import com.example.bookingstore.entity.Author;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private Author savedAuthor;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Given - An author is saved in the database before each test
        Author author = new Author();
        author.setName("J.K. Rowling");
        author.setDeleted(false);

        savedAuthor = authorRepository.save(author);
    }

    @Test
    @Rollback
    void shouldSaveAndFindAuthorById() {
        // When - We fetch the author
        Optional<Author> foundAuthor = authorRepository.findById(savedAuthor.getId());

        // Then - Ensure the author exists and has the correct name
        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getName()).isEqualTo("J.K. Rowling");
    }

    @Test
    @Rollback
    void shouldMarkAuthorAsDeleted() {
        // When - We mark the author as deleted
        authorRepository.markAsDeleted(savedAuthor.getId());

        // Then - Ensure the author is not retrievable using `findByIdDAndDeletedFalse`
        Optional<Author> foundAuthor = authorRepository.findByIdDAndDeletedFalse(savedAuthor.getId());

        assertThat(foundAuthor).isEmpty();
    }

    @Test
    @Rollback
    void shouldUpdateAuthorName() {
        // When - We update the author's name
        authorRepository.updateAuthorsName(savedAuthor.getId(), "George Orwell");

        entityManager.flush(); //synchronize the persistence context with the database
        entityManager.clear(); //is called to clear the first-level cache, ensuring that the subsequent retrieval of the entity reflects the updated state from the database.
        // Then - Ensure the update was successful
        Optional<Author> updatedAuthor = authorRepository.findById(savedAuthor.getId());

        assertThat(updatedAuthor).isPresent();
        assertThat(updatedAuthor.get().getName()).isEqualTo("George Orwell");
    }
}