package com.epam.esm.util;

/**
 * record representing pagination
 * @param page page number
 * @param size maximum number entities per page
 *
 * @author bakhridinova
 */

public record Pagination(Integer page, int size) {
    public Pagination next() {
        return new Pagination(page + 1, size);
    }

    public int getOffset() {
        return size * page;
    }

    public int getLimit() {
        return size;
    }
}
