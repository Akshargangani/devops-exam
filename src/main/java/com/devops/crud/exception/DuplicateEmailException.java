package com.devops.crud.exception;

/**
 * Custom exception thrown when attempting to create or update a user
 * with an email that already exists in the database.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String email, boolean isEmailParam) {
        super(String.format("User with email '%s' already exists", email));
    }

}
