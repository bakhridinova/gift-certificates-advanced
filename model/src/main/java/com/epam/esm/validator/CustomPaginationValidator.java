package com.epam.esm.validator;

import com.epam.esm.dto.extra.FieldName;
import com.epam.esm.exception.CustomValidationException;
import lombok.experimental.UtilityClass;

/**
 * utility class validating pagination details
 *
 * @author bakhridinova
 */

@UtilityClass
public class CustomPaginationValidator {
    /**
     * validates pagination parameters for a search operation to ensure that
     * they contain only digits, are not negative and are between upper and lower bounds
     *
     * @param page int page number to validate
     * @param size int page size to validate
     * @throws CustomValidationException if any of pagination parameters are not valid
     */
    public void validate(int page, int size) {
        validatePage(page);
        validateSize(size);
    }

    void validatePage(int page) {
        CustomValidator.notNegative(FieldName.PAGE, (double) page);
        CustomValidator.notTooLowOrHigh(FieldName.PAGE, (double) page, 0, 10000);
    }

    void validateSize(int size) {
        CustomValidator.notNegative(FieldName.SIZE, (double) size);
        CustomValidator.notTooLowOrHigh(FieldName.SIZE, (double) size, 0, 100);
    }
}
