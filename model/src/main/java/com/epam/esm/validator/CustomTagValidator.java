package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import com.epam.esm.util.FieldName;
import com.epam.esm.exception.CustomValidationException;
import lombok.experimental.UtilityClass;

/**
 * utility class validating tags
 *
 * @author bakhridinova
 */

@UtilityClass
public class CustomTagValidator {
    private final String ONLY_LETTERS = "^[a-zA-Z]*$";

    /**
     * validates tag to ensure that it's parameters are valid
     *
     * @param tag to validate
     * @throws CustomValidationException if any of tag parameters are not valid
     */
    public void validate(TagDto tag) {
        validateName(tag.getName());
    }

    private void validateName(String name) {
        CustomValidator.notNull(FieldName.NAME, name);
        CustomValidator.notEmpty(FieldName.NAME, name);
        CustomValidator.notBlank(FieldName.NAME, name);
        CustomValidator.notTooShortOrLong(FieldName.NAME, name, 3, 30);

        if (!name.matches(ONLY_LETTERS)) {
            throw new CustomValidationException(FieldName.NAME.getName() + " must include only letters");
        }
    }
}
