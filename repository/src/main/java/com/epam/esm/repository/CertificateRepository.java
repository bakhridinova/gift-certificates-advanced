package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;

import java.util.List;

public interface CertificateRepository extends BaseRepository<Certificate> {
    /**
     * retrieves a paginated list of certificates based on pagination parameters
     *
     * @param pagination details
     * @return list of certificates
     */
    List<Certificate> findAll(Pagination pagination);

    /**
     * retrieves total number of certificates in database
     *
     * @return total number of certificates
     */
    Long findTotalNumber();

    /**
     * retrieves certificate with specified ID
     *
     * @param id ID of certificate
     * @return certificate with the specified ID
     */
    Certificate findById(Long id);

    /**
     * retrieves list of certificates that match specified search filter parameters
     *
     * @param searchFilter containing search parameters to apply to the certificates
     * @return list of certificates that match the specified parameters
     */
    List<Certificate> findByFilter(SearchFilter searchFilter);

    /**
     * saves certificate to database
     *
     * @param certificate to save
     */
    void save(Certificate certificate);

    /**
     * deletes certificate from database
     *
     * @param certificate to delete
     */
    void delete(Certificate certificate);
}
