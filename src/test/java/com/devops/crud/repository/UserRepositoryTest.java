package com.devops.crud.repository;

import com.devops.crud.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UserRepository.
 * Uses @DataJpaTest to configure an embedded database for testing.
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save user - should persist user to database")
    void saveUser_Success() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .age(30)
                .address("123 Main St")
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Find by ID - should return user when exists")
    void findById_Success() {
        User user = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .age(25)
                .build();
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Jane", foundUser.get().getFirstName());
    }

    @Test
    @DisplayName("Find by email - should return user when exists")
    void findByEmail_Success() {
        User user = User.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@example.com")
                .age(35)
                .build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("bob.johnson@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("Bob", foundUser.get().getFirstName());
    }

    @Test
    @DisplayName("Exists by email - should return true when email exists")
    void existsByEmail_True() {
        User user = User.builder()
                .firstName("Alice")
                .lastName("Williams")
                .email("alice.williams@example.com")
                .age(28)
                .build();
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("alice.williams@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Exists by email - should return false when email does not exist")
    void existsByEmail_False() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Find by first name containing - should return matching users")
    void findByFirstNameContainingIgnoreCase_Success() {
        User user1 = User.builder()
                .firstName("Johnny")
                .lastName("Doe")
                .email("johnny.doe@example.com")
                .age(30)
                .build();
        User user2 = User.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@example.com")
                .age(25)
                .build();
        User user3 = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .age(28)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> foundUsers = userRepository.findByFirstNameContainingIgnoreCase("John");

        assertEquals(2, foundUsers.size());
    }

    @Test
    @DisplayName("Find by age greater than - should return matching users")
    void findByAgeGreaterThan_Success() {
        User user1 = User.builder()
                .firstName("Young")
                .lastName("Person")
                .email("young@example.com")
                .age(20)
                .build();
        User user2 = User.builder()
                .firstName("Middle")
                .lastName("Aged")
                .email("middle@example.com")
                .age(35)
                .build();
        User user3 = User.builder()
                .firstName("Senior")
                .lastName("Citizen")
                .email("senior@example.com")
                .age(60)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> foundUsers = userRepository.findByAgeGreaterThan(30);

        assertEquals(2, foundUsers.size());
    }

    @Test
    @DisplayName("Delete user - should remove user from database")
    void deleteUser_Success() {
        User user = User.builder()
                .firstName("ToDelete")
                .lastName("User")
                .email("delete@example.com")
                .age(40)
                .build();
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @DisplayName("Find all - should return all users")
    void findAll_Success() {
        User user1 = User.builder()
                .firstName("User1")
                .lastName("Test")
                .email("user1@example.com")
                .age(25)
                .build();
        User user2 = User.builder()
                .firstName("User2")
                .lastName("Test")
                .email("user2@example.com")
                .age(30)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();

        assertTrue(allUsers.size() >= 2);
    }

}
