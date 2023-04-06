package com.epam.esm.exception;

/**
 * custom exception for "not found" error
 *
 * @author bakhridinova
 */

public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(String message) {
        super(message);
    }
}
