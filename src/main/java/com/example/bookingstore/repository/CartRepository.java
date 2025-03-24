package com.example.bookingstore.repository;

import com.example.bookingstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT c from Cart c where c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

}