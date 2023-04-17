package com.epam.esm.validator;

import com.epam.esm.exception.CustomValidationException;
import com.epam.esm.util.FieldName;
import lombok.experimental.UtilityClass;

/**
 * utility class validating user input details
 *
 * @author bakhridinova
 */

@UtilityClass
public class CustomValidator {
    private final String ONLY_LETTERS_AND_SPACES = "^[a-zA-Z ]*$";

    /**
     * validates ID parameter to ensure that it is not null and is positive
     *
     * @param fieldName field to validate
     * @param id Long ID to validate
     * @throws CustomValidationException if ID parameter is not valid
     */
    public void validateId(FieldName fieldName, Long id) {
        CustomValidator.notNull(fieldName, id);

        if (id <= 0) {
            throw new CustomValidationException(fieldName.getName() + " must be positive");
        }
    }

    void notNull(FieldName fieldName, Object value) {
        if (value == null) {
            throw new CustomValidationException(fieldName.getName() + " should not be null");
        }
    }

    void notBlank(FieldName fieldName, String value) {
        if (value.isBlank()) {
            throw new CustomValidationException(fieldName.getName() + " should not be empty or blank");
        }
    }

    void onlyLettersAndSpaces(FieldName fieldName, String value) {
        if (!value.matches(ONLY_LETTERS_AND_SPACES)) {
            throw new CustomValidationException(fieldName.getName() + " must include only letters and spaces");
        }
    }

    void notTooShortOrLong(FieldName fieldName, String value, int lower, int upper) {
        if (value.length() < lower || value.length() > upper) {
            throw new CustomValidationException(fieldName.getName() + " must be between " + lower + " and " + upper + " characters");
        }
    }

    void notTooLowOrHigh(FieldName fieldName, Double value, int lower, int upper) {
        if (value < lower || value > upper) {
            throw new CustomValidationException(fieldName.getName() + " must be between " + lower + " and " + upper );
        }
    }

     void notNegative(FieldName fieldName, Double value) {
        if (value < 0) {
            throw new CustomValidationException(fieldName.getName() + " should not be negative");
        }
    }
}
