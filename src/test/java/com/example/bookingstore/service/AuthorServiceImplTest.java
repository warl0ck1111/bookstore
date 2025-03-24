package com.example.bookingstore.service;

import com.example.bookingstore.dto.request.AuthorDto;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.exceptions.ResourceNotFoundException;
import com.example.bookingstore.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAuthor() {
        // Given
        AuthorDto authorDto = new AuthorDto("J.K. Rowling");
        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setName("J.K. Rowling");

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        // When
        Author createdAuthor = authorService.createAuthor(authorDto);

        // Then
        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId()).isEqualTo(1L);
        assertThat(createdAuthor.getName()).isEqualTo("J.K. Rowling");

        // Verify repository interaction
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void shouldUpdateAuthorsName() {
        // Given
        Long authorId = 1L;
        String newName = "George Orwell";

        // When
        authorService.updateAuthorsName(authorId, newName);

        // Then
        verify(authorRepository, times(1)).updateAuthorsName(authorId, newName);
    }

    @Test
    void shouldFindAuthorById() {
        // Given
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("J.K. Rowling");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // When
        Author foundAuthor = authorService.findById(authorId);

        // Then
        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getId()).isEqualTo(authorId);
        assertThat(foundAuthor.getName()).isEqualTo("J.K. Rowling");

        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        // Given
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> authorService.findById(authorId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found");

        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void shouldDeleteAuthor() {
        // Given
        Long authorId = 1L;

        // When
        authorService.deleteAuthor(authorId);

        // Then
        verify(authorRepository, times(1)).markAsDeleted(authorId);
    }
}