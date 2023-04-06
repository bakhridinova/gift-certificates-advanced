package com.epam.esm.repository;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CustomRepository<Tag> {
    /**
     * retrieves a paginated list of tags based on pagination parameters
     *
     * @param pagination Pagination details
     * @return list of tags
     */
    List<Tag> findAll(Pagination pagination);

    /**
     * retrieves tag with specified ID
     *
     * @param id Long ID of tag
     * @return tag with the specified ID
     */
    Tag findById(Long id);

    /**
     * retrieves most widely used tag of user who has
     * maximum sum of all orders
     *
     * @return specified tag
     */
    Tag findMostWidelyUsedTagOfAUserWithTheHighestCostOfAllOrders();

    /**
     * checks if tag with given name already exists in the database.
     *
     * @param name name of tag to check for existence
     * @return true if tag with given name exists, otherwise false
     */
    boolean exists(String name);

    /**
     * saves tag to database
     *
     * @param tag Tag to save
     */
    void save(Tag tag);

    /**
     * deletes tag from database
     *
     * @param tag Tag to delete
     */
    void delete(Tag tag);
}
