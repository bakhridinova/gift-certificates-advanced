package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;

import java.util.List;

/**
 * interface representing certificate related methods
 *
 * @author bakhridinova
 */

public interface CertificateService {
    /**
     * retrieves paginated list of all certificates
     *
     * @param pagination page and size
     * @return certificates that match pagination criteria
     */
    List<CertificateDto> findAll(Pagination pagination);

    /**
     * retrieves certificate  by its ID
     *
     * @param id of certificate to retrieve
     * @return certificate with given ID
     */
    CertificateDto findById(Long id);

    /**
     * retrieves list of certificates that match specified search criteria
     *
     * @param searchFilter containing filter criteria to search for
     * @return list of certificates that match search criteria
     */
    List<CertificateDto> findByFilter(SearchFilter searchFilter);

    /**
     * updates the name of certificate with specified ID
     *
     * @param id ID of certificate to update
     * @param certificate certificate containing updated name
     * @return updated certificate
     */
    CertificateDto updateName(Long id, CertificateDto certificate);

    /**
     * creates new certificate
     *
     * @param certificate to create
     * @return created certificate with its ID and other fields populated
     */
    CertificateDto create(CertificateDto certificate);

    /**
     * deletes certificate with given ID
     *
     * @param id of certificate to delete
     */
    void delete(Long id);
}
