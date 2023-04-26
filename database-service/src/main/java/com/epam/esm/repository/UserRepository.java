package com.epam.esm.repository;

import com.epam.esm.entity.User;
import com.epam.esm.util.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User> {
    /**
     * retrieves a paginated list of users based on pagination parameters
     *
     * @param pagination details
     * @return list of users
     */
    List<User> findAllByPage(Pagination pagination);

    /**
     * retrieves user with specified ID
     *
     * @param id ID of user
     * @return user with the specified ID
     */
    User findById(Long id);
}
