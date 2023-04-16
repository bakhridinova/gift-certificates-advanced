package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.util.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CustomRepository<Order> {
    /**
     * retrieves a paginated list of orders based on pagination parameters
     *
     * @param pagination details
     * @return list of orders
     */
    List<Order> findAll(Pagination pagination);

    /**
     * retrieves order with specified ID
     *
     * @param id ID of order
     * @return order with the specified ID
     */
    Order findById(Long id);

    /**
     * retrieves a list of orders associated with certificate, based on pagination details
     *
     * @param certificate to search for associated orders
     * @param pagination details
     * @return list of orders of specified certificate
     */
    List<Order> findByCertificate(Certificate certificate, Pagination pagination);


    /**
     * retrieves a list of orders associated with user, based on pagination details.
     *
     * @param user to search for associated orders
     * @param pagination details
     * @return list of orders of specified user
     */
    List<Order> findByUser(User user, Pagination pagination);

    /**
     * saves order to database
     *
     * @param order to save
     */
    void save(Order order);
}
