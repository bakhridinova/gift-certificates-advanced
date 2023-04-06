package com.epam.esm.dto.extra;

/**
 * record representing pagination
 * @param page page number
 * @param size maximum number entities per page
 *
 * @author bakhridinova
 */

public record Pagination(int page, int size) {
    public int getOffset() {
        return size * page;
    }

    public int getLimit() {
        return size;
    }
}
