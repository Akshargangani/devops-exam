package com.devops.crud.exception;

/**
 * Custom exception thrown when a user is not found in the database.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with id: %d", userId));
    }

}
