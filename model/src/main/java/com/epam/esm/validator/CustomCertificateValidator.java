package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.extra.FieldName;
import com.epam.esm.exception.CustomValidationException;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * utility class validating certificates
 *
 * @author bakhridinova
 */

@UtilityClass
public class CustomCertificateValidator {
    /**
     * validates certificate to ensure that it's parameters are valid
     *
     * @param certificate CertificateDTO to validate
     * @throws CustomValidationException if any of certificate parameters are not valid
     */
    public void validate(CertificateDTO certificate) {
        validateName(certificate.getName());
        validateDescription(certificate.getDescription());
        validatePrice(certificate.getPrice());
        validateDuration(certificate.getDuration());
        validateTags(certificate.getTags());
    }

    /**
     * validates certificate name to ensure that it's not null, empty or blank,
     * includes required number of characters and does not include special characters
     *
     * @param name String name to validate
     * @throws CustomValidationException if name is not valid
     */
    public void validateName(String name) {
        CustomValidator.notNull(FieldName.NAME, name);
        CustomValidator.notEmpty(FieldName.NAME, name);
        CustomValidator.notBlank(FieldName.NAME, name);
        CustomValidator.notTooShortOrLong(FieldName.NAME, name, 3, 20);
        CustomValidator.onlyLettersAndSpaces(FieldName.NAME, name);
    }

    void validateDescription(String description) {
        CustomValidator.notNull(FieldName.DESCRIPTION, description);
        CustomValidator.notEmpty(FieldName.DESCRIPTION, description);
        CustomValidator.notBlank(FieldName.DESCRIPTION, description);
        CustomValidator.notTooShortOrLong(FieldName.DESCRIPTION, description, 5, 40);
        CustomValidator.onlyLettersAndSpaces(FieldName.DESCRIPTION, description);

    }

    void validatePrice(Double price) {
        CustomValidator.notNull(FieldName.PRICE, price);
        CustomValidator.notNegative(FieldName.PRICE, price);
        CustomValidator.notTooLowOrHigh(FieldName.PRICE, price, 1, 100);
    }

    void validateDuration(Integer duration) {
        CustomValidator.notNull(FieldName.DURATION, duration);
        CustomValidator.notNegative(FieldName.DURATION, Double.valueOf(duration));
        CustomValidator.notTooLowOrHigh(FieldName.DURATION, Double.valueOf(duration), 7, 90);
    }

    void validateTags(Set<TagDTO> tags) {
        CustomValidator.notNull(FieldName.TAGS, tags);
        tags.forEach(CustomTagValidator::validate);
    }
}
