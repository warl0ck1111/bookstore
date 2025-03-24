package com.example.bookingstore.entity;

import com.example.bookingstore.enums.Genre;
import com.example.bookingstore.exceptions.InsufficientStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only numbers and letters")
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;


    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dash")
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    private Integer yearOfPublication;

    private Double price;

    private Integer stock;

    private Boolean deleted = Boolean.FALSE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Book(String title, Genre genre, String isbn, Author author, Integer yearOfPublication, Integer stock, Double price) {
        this.title = title;
        this.genre = genre;
        this.isbn = isbn;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.stock = stock;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}