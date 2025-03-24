package com.example.bookingstore.service;

import com.example.bookingstore.dto.request.AuthorDto;
import com.example.bookingstore.entity.Author;
import com.example.bookingstore.exceptions.ResourceNotFoundException;
import com.example.bookingstore.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author createAuthor(AuthorDto author) {
        log.info("createAuthor/author: {}", author);
        Author authorEntity = new Author();
        authorEntity.setName(author.name());
        return authorRepository.save(authorEntity);
    }

    @Override
    public void updateAuthorsName(Long id, String name) {
        log.info("updateAuthorsName/authors id: {}   name{}", id, name);
        authorRepository.updateAuthorsName(id, name);
    }

    @Override
    public Author findById(Long id) {
        log.info("findById/authorId: {}", id);
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    }

    @Override
    public void deleteAuthor(Long authorId) {
        log.info("Deleting author: {}", authorId);
        authorRepository.markAsDeleted(authorId);

    }

}
