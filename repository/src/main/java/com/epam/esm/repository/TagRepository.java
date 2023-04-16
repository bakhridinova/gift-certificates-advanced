package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.util.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CustomRepository<Tag> {
    /**
     * retrieves a paginated list of tags based on pagination parameters
     *
     * @param pagination details
     * @return list of tags
     */
    List<Tag> findAll(Pagination pagination);

    /**
     * retrieves tag with specified ID
     *
     * @param id ID of tag
     * @return tag with the specified ID
     */
    Tag findById(Long id);

    /**
     * retrieves most widely used tag of user who has
     * maximum sum of all orders
     *
     * @return specified tag
     */
    Tag findSpecial();

    /**
     * checks if tag with given name already exists in the database.
     *
     * @param name of tag to check for existence
     * @return true if tag with given name exists, otherwise false
     */
    boolean exists(String name);

    /**
     * saves tag to database
     *
     * @param tag to save
     */
    void save(Tag tag);

    /**
     * deletes tag from database
     *
     * @param tag to delete
     */
    void delete(Tag tag);
}
