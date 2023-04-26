package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends BaseRepository<Tag> {
    /**
     * retrieves a paginated list of tags based on pagination parameters
     *
     * @param pagination details
     * @return list of tags
     */
    List<Tag> findAllByPage(Pagination pagination);

    /**
     * retrieves tag with specified ID
     *
     * @param id ID of tag
     * @return tag with the specified ID
     */
    Tag findById(Long id);

    /**
     * retrieves tag with specified name
     *
     * @param name of tag
     * @return optional of tag with the specified name
     */
    Optional<Tag> findByName(String name);

    /**
     * retrieves most widely used tag of user who has
     * maximum sum of all orders
     *
     * @return specified tag
     */
    Tag findSpecial();

    /**
     * saves tag to database
     *
     * @param tag to save
     */
    void save(Tag tag);

    /**
     * attaches tags to certificates
     *
     * @param certificate to attach to
     * @param tags to attach
     */
    void setTags(Certificate certificate, Set<Tag> tags);

    /**
     * deletes tag from database
     *
     * @param tag to delete
     */
    void delete(Tag tag);
}
