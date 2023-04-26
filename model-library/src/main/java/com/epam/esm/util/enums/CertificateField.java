package com.epam.esm.util.enums;

import com.epam.esm.util.FieldName;

/**
 * enum representing certificate fields
 *
 * @author bakhridinova
 */

public enum CertificateField implements FieldName {
    ID,
    NAME,
    DESCRIPTION,
    PRICE,
    DURATION,
    TAGS;

    @Override
    public String getName() {
        return "certificate " + this.name().toLowerCase();
    }
}
