package com.example.bookingstore.config;


import com.example.bookingstore.entity.Author;
import com.example.bookingstore.entity.Book;
import com.example.bookingstore.entity.User;
import com.example.bookingstore.enums.Genre;
import com.example.bookingstore.repository.AuthorRepository;
import com.example.bookingstore.repository.BookRepository;
import com.example.bookingstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Override
    public void run(String... args) {
        if (bookRepository.count() < 10) {

            Author author = authorRepository.save(new Author("Ruskin Bond"));

            // Create books
            List<Book> books = List.of(
                    new Book("The Room on the Roof", Genre.FICTION, "1-84205-167-3", author, 1863, 10, 2000.0),
                    new Book("Rusty Runs Away", Genre.HORROR, "1-84205-167-4", author, 1953, 15, 2500.0),
                    new Book("The Blue Umbrella", Genre.FICTION, "1-84205-167-5", author, 1974, 12, 1800.0),
                    new Book("Our Trees Still Grow in Dehra", Genre.POETRY, "1-84205-167-6", author, 1991, 8, 2200.0),
                    new Book("Delhi Is Not Far", Genre.SATIRE, "1-84205-167-7", author, 1994, 9, 1900.0),
                    new Book("A Flight of Pigeons", Genre.FICTION, "1-84205-167-8", author, 1978, 10, 2100.0),
                    new Book("Angry River", Genre.THRILLER, "1-84205-167-9", author, 1972, 14, 2300.0),
                    new Book("The Night Train at Deoli", Genre.FICTION, "1-84205-168-0", author, 1988, 7, 2400.0),
                    new Book("Time Stops at Shamli", Genre.THRILLER, "1-84205-168-1", author, 1989, 10, 2000.0),
                    new Book("Roads to Mussoorie", Genre.FICTION, "1-84205-168-2", author, 2003, 6, 2600.0)
            );

            bookRepository.saveAll(books);
            log.info("Preloaded 10 books into the database.");
        }

        if (userRepository.count() < 4) {
            List<User> users = List.of(
                    new User("Ironman", "$2a$10$3Ket22UJh4Msv3qjynPyne3PFTWbvYLIR9Ca27MPEarCA9.qi.pWu"),
                    new User("Superman", "$2a$10$3Ket22UJh4Msv3qjynPyne3PFTWbvYLIR9Ca27MPEarCA9.qi.pWu"),
                    new User("Lukman", "$2a$10$3Ket22UJh4Msv3qjynPyne3PFTWbvYLIR9Ca27MPEarCA9.qi.pWu"),
                    new User("Suleman", "$2a$10$3Ket22UJh4Msv3qjynPyne3PFTWbvYLIR9Ca27MPEarCA9.qi.pWu"));
            userRepository.saveAll(users);
        }
    }
}