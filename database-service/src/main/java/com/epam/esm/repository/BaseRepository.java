package com.epam.esm.repository;

import com.epam.esm.entity.Identifiable;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 * wrapper repository for entities
 *
 * @author bakhridinova
 */

public interface BaseRepository<T extends Identifiable> {
    /**
     * retrieves a paginated list of entities based on pagination parameters
     *
     * @param pagination details
     * @return list of entities
     */
    List<T> findAllByPage(Pagination pagination);

    /**
     * retrieves total number of entities in database
     *
     * @return total number of entities
     */
    Long findTotalNumber();

    /**
     * retrieves entity with specified ID
     *
     * @param id ID of entity
     * @return entity with the specified ID
     */
    T findById(Long id);

    /**
     * saves entity to database
     *
     * @param t entity to save
     */
    default void save(T t) {

    }

    /**
     * deletes entity from database
     *
     * @param t entity to delete
     */
    default void delete(T t) {

    }
}
