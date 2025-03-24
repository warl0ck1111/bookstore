
package com.example.bookingstore.repository;

import com.example.bookingstore.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Given - Creating and saving a user
        User user = new User();
        user.setUsername("testUser");
        User savedUser = userRepository.save(user);
    }

    @Test
    @Rollback
    void shouldCheckIfUsernameExistsIgnoreCase() {
        // When - Checking username existence
        boolean exists = userRepository.existsByUsernameIgnoreCase("TESTUSER");

        // Then - Validate the username exists (case insensitive)
        assertThat(exists).isTrue();
    }

    @Test
    @Rollback
    void shouldLoadUserByUsername() {
        // When - Fetching user by username
        Optional<User> foundUser = userRepository.loadUserByUsername("testUser");

        // Then - Validate user exists and has correct username
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
    }

    @Test
    @Rollback
    void shouldReturnEmptyIfUsernameDoesNotExist() {
        // When - Trying to fetch a non-existent username
        Optional<User> foundUser = userRepository.loadUserByUsername("nonexistent");

        // Then - Validate user is not found
        assertThat(foundUser).isEmpty();
    }
}