package com.devops.crud.service;

import com.devops.crud.dto.UserDTO;
import com.devops.crud.entity.User;
import com.devops.crud.exception.DuplicateEmailException;
import com.devops.crud.exception.UserNotFoundException;
import com.devops.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for User operations.
 * Implements business logic and coordinates between controller and repository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * Create a new user.
     *
     * @param userDTO the user data to create
     * @return the created user DTO
     * @throws DuplicateEmailException if email already exists
     */
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateEmailException(userDTO.getEmail(), true);
        }

        User user = mapToEntity(userDTO);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());
        return mapToDTO(savedUser);
    }

    /**
     * Get all users.
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToDTO(user);
    }

    /**
     * Get user by email.
     *
     * @param email the user email
     * @return the user DTO
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToDTO(user);
    }

    /**
     * Update an existing user.
     *
     * @param id      the user ID
     * @param userDTO the updated user data
     * @return the updated user DTO
     * @throws UserNotFoundException   if user not found
     * @throws DuplicateEmailException if email already exists for another user
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userDTO.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new DuplicateEmailException(userDTO.getEmail(), true);
            }
        }

        // Update user fields
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAge(userDTO.getAge());
        existingUser.setAddress(userDTO.getAddress());

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with id: {}", updatedUser.getId());

        return mapToDTO(updatedUser);
    }

    /**
     * Delete a user by ID.
     *
     * @param id the user ID
     * @throws UserNotFoundException if user not found
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    /**
     * Search users by first name (case-insensitive).
     *
     * @param firstName the first name to search
     * @return list of matching users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> searchByFirstName(String firstName) {
        log.info("Searching users by first name: {}", firstName);
        return userRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search users by last name (case-insensitive).
     *
     * @param lastName the last name to search
     * @return list of matching users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> searchByLastName(String lastName) {
        log.info("Searching users by last name: {}", lastName);
        return userRepository.findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get users with age greater than specified value.
     *
     * @param age the age threshold
     * @return list of users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByAgeGreaterThan(Integer age) {
        log.info("Fetching users with age greater than: {}", age);
        return userRepository.findByAgeGreaterThan(age)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if a user exists by ID.
     *
     * @param id the user ID
     * @return true if user exists
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Map User entity to UserDTO.
     */
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .age(user.getAge())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Map UserDTO to User entity.
     */
    private User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .age(userDTO.getAge())
                .address(userDTO.getAddress())
                .build();
    }

}
