package com.epam.esm.exception;

/**
 * custom exception for "validation failed" error
 *
 * @see com.epam.esm.validator
 * @author bakhridinova
 */

public class CustomValidationException extends RuntimeException {
    public CustomValidationException(String message) {
        super(message);
    }
}
