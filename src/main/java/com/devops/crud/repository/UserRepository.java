package com.devops.crud.repository;

import com.devops.crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository interface for performing CRUD operations on User entity.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email address.
     *
     * @param email the email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given email.
     *
     * @param email the email address to check
     * @return true if a user exists with this email
     */
    boolean existsByEmail(String email);

    /**
     * Find users by first name (case-insensitive search).
     *
     * @param firstName the first name to search for
     * @return list of users matching the first name
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Find users by last name (case-insensitive search).
     *
     * @param lastName the last name to search for
     * @return list of users matching the last name
     */
    List<User> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Find users by age greater than the specified value.
     *
     * @param age the age threshold
     * @return list of users with age greater than specified value
     */
    List<User> findByAgeGreaterThan(Integer age);

}
