package com.epam.esm.hateoas;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * interface providing methods for adding HATEOAS links to entity/entities
 *
 * @author bakhridinova
 */

public interface HateoasAdder<T extends RepresentationModel<T>> {
    void addLinksToEntity(T entity);

    default void addLinksToEntityList(List<T> entities) {
        entities.forEach(this::addLinksToEntity);
    }
}
