package com.example.bookingstore.repository;

import com.example.bookingstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Modifying
    @Query("UPDATE Author a SET a.deleted = true WHERE a.id = :authorId")
    void markAsDeleted(@Param("authorId") Long authorId);


    @Query("Select a from  Author a WHERE a.deleted = false and a.id =:authorId")
    Optional<Author> findByIdDAndDeletedFalse(@Param("authorId") Long authorId);


    @Modifying
    @Query("UPDATE Author a SET a.name = :name WHERE a.id = :authorId")
    void updateAuthorsName(@Param("authorId") Long authorId, String name);

}