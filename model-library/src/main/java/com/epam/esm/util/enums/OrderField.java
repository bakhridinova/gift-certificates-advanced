package com.epam.esm.util.enums;

import com.epam.esm.util.FieldName;

/**
 * enum representing order fields
 *
 * @author bakhridinova
 */

public enum OrderField implements FieldName {
    ID;

    @Override
    public String getName() {
        return "order " + this.name().toLowerCase();
    }
}
