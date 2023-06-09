package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.util.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order> {
    /**
     * retrieves a paginated list of orders based on pagination parameters
     *
     * @param pagination details
     * @return list of orders
     */
    List<Order> findAllByPage(Pagination pagination);

    /**
     * retrieves total number of orders in database
     *
     * @return total number of orders
     */
    Long findTotalNumber();

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
     * @param certificateId to search for associated orders
     * @param pagination details
     * @return list of orders of specified certificate
     */
    List<Order> findByCertificateId(Long certificateId, Pagination pagination);


    /**
     * retrieves a list of orders associated with user, based on pagination details.
     *
     * @param userId to search for associated orders
     * @param pagination details
     * @return list of orders of specified user
     */
    List<Order> findByUserId(Long userId, Pagination pagination);

    /**
     * saves order to database
     *
     * @param order to save
     */
    void save(Order order);
}
