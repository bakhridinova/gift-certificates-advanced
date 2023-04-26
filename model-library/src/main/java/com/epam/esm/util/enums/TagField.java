package com.epam.esm.util.enums;

import com.epam.esm.util.FieldName;

/**
 * enum representing tag fields
 *
 * @author bakhridinova
 */

public enum TagField implements FieldName {
    ID,
    NAME;

    @Override
    public String getName() {
        return "tag " + this.name().toLowerCase();
    }
}
