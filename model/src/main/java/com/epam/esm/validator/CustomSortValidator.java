package com.epam.esm.validator;

import com.epam.esm.dto.extra.FieldName;
import com.epam.esm.exception.CustomValidationException;
import lombok.experimental.UtilityClass;

/**
 * utility class validating sorting details
 *
 * @author bakhridinova
 */

@UtilityClass
public class CustomSortValidator {
    private final String ASC_OR_DESC = "(asc|desc)";
    private final String ID_NAME_CREATE_DATE_OR_LAST_UPDATE_DATE = "(id|name|price|duration|createdAt|lastUpdatedAt)";

    /**
     * validates the sorting parameters to ensure that they are not null,
     * empty or blank and are equal to any of required values
     *
     * @param sortType String sort type to validate
     * @param sortOrder String sort order to validate
     * @throws CustomValidationException if any of sorting parameters are not valid
     */
    public void validate(String sortType, String sortOrder) {
        validateSortType(sortType);
        validateSortOrder(sortOrder);
    }

    void validateSortOrder(String order) {
        CustomValidator.notNull(FieldName.ORDER, order);
        CustomValidator.notEmpty(FieldName.ORDER, order);
        CustomValidator.notBlank(FieldName.ORDER, order);

        if (!order.matches(ASC_OR_DESC)) {
            throw new CustomValidationException(FieldName.ORDER.getName() + " must be either asc or desc");
        }
    }

    void validateSortType(String type) {
        CustomValidator.notNull(FieldName.TYPE, type);
        CustomValidator.notEmpty(FieldName.TYPE, type);
        CustomValidator.notBlank(FieldName.TYPE, type);

        if (!type.matches(ID_NAME_CREATE_DATE_OR_LAST_UPDATE_DATE)) {
            throw new CustomValidationException(FieldName.TYPE.getName() + " must be either name, price, duration, createdAt or lastUpdatedAt");
        }
    }
}
