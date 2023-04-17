package com.epam.esm.util.enums;

import com.epam.esm.util.FieldName;

/**
 * enum representing sort fields
 *
 * @author bakhridinova
 */

public enum SortField implements FieldName {
    TYPE,
    ORDER;

    @Override
    public String getName() {
        return "sort " + this.name().toLowerCase();
    }
}
