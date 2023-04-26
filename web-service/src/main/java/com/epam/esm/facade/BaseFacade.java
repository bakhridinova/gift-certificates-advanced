package com.epam.esm.facade;

import com.epam.esm.exception.CustomMessageHolder;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * interface hiding validation and hateoas operations on entity/entities
 *
 * @author bakhridinova
 */

public interface BaseFacade<T extends RepresentationModel<T>> {
    List<T> findAllByPage(int page, int size);
    T findById(Long id);

    default T create(T t) {
        return null;
    }

    default CustomMessageHolder deleteById(Long id) {
        return null;
    }
}
