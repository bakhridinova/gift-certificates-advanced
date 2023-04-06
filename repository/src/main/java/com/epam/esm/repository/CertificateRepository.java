package com.epam.esm.repository;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.dto.extra.SearchFilter;
import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateRepository extends CustomRepository<Certificate> {
    /**
     * retrieves a paginated list of certificates based on pagination parameters
     *
     * @param pagination Pagination details
     * @return list of certificates
     */
    List<Certificate> findAll(Pagination pagination);

    /**
     * retrieves certificate with specified ID
     *
     * @param id Long ID of certificate
     * @return certificate with the specified ID
     */
    Certificate findById(Long id);

    /**
     * retrieves list of certificates that match specified search filter parameters
     *
     * @param searchFilter SearchFilter containing search parameters to apply to the certificates
     * @return list of certificates that match the specified parameters
     */
    List<Certificate> findByFilter(SearchFilter searchFilter);

    /**
     * saves certificate to database
     *
     * @param certificate Certificate to save
     */
    void save(Certificate certificate);

    /**
     * deletes certificate from database
     *
     * @param certificate Certificate to delete
     */
    void delete(Certificate certificate);
}
