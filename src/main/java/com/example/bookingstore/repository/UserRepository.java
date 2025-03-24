package com.example.bookingstore.repository;

import com.example.bookingstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select (count(u.username) > 0) from User u where lower( u.username) = lower(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);

    @Query("select new User(u.id, u.username) from User u where LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    Optional<User> loadUserByUsername(@Param("username") String username);
}
