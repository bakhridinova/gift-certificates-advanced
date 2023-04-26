package com.epam.esm.exception;

/**
 * custom exception for "unique constraint" error
 *
 * @author bakhridinova
 */

public class CustomEntityAlreadyExistsException extends RuntimeException {

    public CustomEntityAlreadyExistsException(String message) {
        super(message);
    }
}
