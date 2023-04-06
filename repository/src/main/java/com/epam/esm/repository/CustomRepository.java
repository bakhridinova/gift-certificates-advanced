package com.epam.esm.repository;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.Identifiable;

import java.util.List;

/**
 * wrapper repository for entities
 *
 * @author bakhridinova
 */

public interface CustomRepository<T extends Identifiable> {
    /**
     * retrieves a paginated list of entities based on pagination parameters
     *
     * @param pagination Pagination details
     * @return list of entities
     */
    List<T> findAll(Pagination pagination);

    /**
     * retrieves entity with specified ID
     *
     * @param id Long ID of entity
     * @return entity with the specified ID
     */
    T findById(Long id);

    /**
     * saves entity to database
     *
     * @param t T entity to save
     */
    void save(T t);

    /**
     * deletes entity from database
     *
     * @param t T entity to delete
     */
    void delete(T t);
}
