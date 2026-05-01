package com.devops.crud.controller;

import com.devops.crud.dto.ApiResponse;
import com.devops.crud.dto.UserDTO;
import com.devops.crud.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User CRUD operations.
 * Provides endpoints for creating, reading, updating, and deleting users.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Enable CORS for all origins (configure appropriately for production)
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     * POST /api/v1/users
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("POST /api/v1/users - Creating new user");
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", createdUser));
    }

    /**
     * Get all users.
     * GET /api/v1/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("GET /api/v1/users - Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get user by ID.
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("GET /api/v1/users/{} - Fetching user", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Get user by email.
     * GET /api/v1/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        log.info("GET /api/v1/users/email/{} - Fetching user by email", email);
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Update an existing user.
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        log.info("PUT /api/v1/users/{} - Updating user", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    /**
     * Delete a user.
     * DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/v1/users/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    /**
     * Search users by first name.
     * GET /api/v1/users/search/firstname/{firstName}
     */
    @GetMapping("/search/firstname/{firstName}")
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchByFirstName(@PathVariable String firstName) {
        log.info("GET /api/v1/users/search/firstname/{} - Searching users", firstName);
        List<UserDTO> users = userService.searchByFirstName(firstName);
        return ResponseEntity.ok(ApiResponse.success("Search completed", users));
    }

    /**
     * Search users by last name.
     * GET /api/v1/users/search/lastname/{lastName}
     */
    @GetMapping("/search/lastname/{lastName}")
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchByLastName(@PathVariable String lastName) {
        log.info("GET /api/v1/users/search/lastname/{} - Searching users", lastName);
        List<UserDTO> users = userService.searchByLastName(lastName);
        return ResponseEntity.ok(ApiResponse.success("Search completed", users));
    }

    /**
     * Get users with age greater than specified value.
     * GET /api/v1/users/age/greater-than/{age}
     */
    @GetMapping("/age/greater-than/{age}")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByAgeGreaterThan(@PathVariable Integer age) {
        log.info("GET /api/v1/users/age/greater-than/{} - Fetching users", age);
        List<UserDTO> users = userService.getUsersByAgeGreaterThan(age);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Health check endpoint.
     * GET /api/v1/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Service is running", "UP"));
    }

}
