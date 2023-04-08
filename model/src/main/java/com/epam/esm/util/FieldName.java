package com.epam.esm.util;

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

    TYPE {
        @Override
        public String getName() {
            return "sortType";
        }
    },
    ORDER {
        @Override
        public String getName() {
            return "sortOrder";
        }
    };

    public String getName() {
        return this.name().toLowerCase();
    }
}
