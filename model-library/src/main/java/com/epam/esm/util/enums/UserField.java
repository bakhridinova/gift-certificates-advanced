package com.epam.esm.util.enums;

import com.epam.esm.util.FieldName;

/**
 * enum representing user fields
 *
 * @author bakhridinova
 */

public enum UserField implements FieldName {
    ID;

    @Override
    public String getName() {
        return "user " + this.name().toLowerCase();
    }
}
