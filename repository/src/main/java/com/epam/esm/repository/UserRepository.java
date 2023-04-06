package com.epam.esm.repository;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CustomRepository<User> {
    /**
     * retrieves a paginated list of users based on pagination parameters
     *
     * @param pagination Pagination details
     * @return list of users
     */
    List<User> findAll(Pagination pagination);

    /**
     * retrieves user with specified ID
     *
     * @param id Long ID of user
     * @return user with the specified ID
     */
    User findById(Long id);

    /**
     * saves user to database
     *
     * @param user User to save
     */
    void save(User user);

    /**
     * deletes user from database
     *
     * @param user User to delete
     */
    void delete(User user);
}
