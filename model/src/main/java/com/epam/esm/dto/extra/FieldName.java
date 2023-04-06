package com.epam.esm.dto.extra;

/**
 * enum providing constant values for different field names
 * used by custom validators
 *
 * @author bakhridinova
 */

public enum FieldName {
    ID,

    NAME,
    DESCRIPTION,
    PRICE,
    DURATION,
    TAGS,

    PAGE,
    SIZE,

    TYPE,
    ORDER;

    public String getName() {
        return this.name().toLowerCase();
    }
}
