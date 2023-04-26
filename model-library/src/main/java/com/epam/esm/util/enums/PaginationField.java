package com.epam.esm.util.enums;

import com.epam.esm.util.FieldName;

/**
 * enum representing pagination fields
 *
 * @author bakhridinova
 */

public enum PaginationField implements FieldName {
    NUMBER,
    SIZE;


    @Override
    public String getName() {
        return "page " + this.name().toLowerCase();
    }
}
