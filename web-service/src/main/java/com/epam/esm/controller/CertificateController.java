package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.facade.CertificateFacade;
import com.epam.esm.util.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateFacade certificateFacade;

    /**
     * GET endpoint to retrieve list of certificates
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of certificates
     */
    @GetMapping
    public List<CertificateDto> getAllByPage(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size) {
        return certificateFacade.findAllByPage(page, size);
    }

    /**
     * GET endpoint to retrieve specific certificate by its ID
     *
     * @param id long ID of required certificate
     * @return specified certificate
     */
    @GetMapping("/{id}")
    public CertificateDto getById(@PathVariable long id) {
        return certificateFacade.findById(id);
    }

    /**
     * GET endpoint to search for certificates based on search parameters
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @param searchFilter holding search parameters
     * @return List of certificates based on provided search parameters
     */
    @GetMapping("/search")
    public List<CertificateDto> search(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestBody(required = false) SearchFilter searchFilter) {
        return certificateFacade.findByFilter(searchFilter, page, size);
    }

    /**
     * GET endpoint to search for retrieving orders associated with specific certificate
     *
     * @param id ID of certificate for which to retrieve orders
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of orders associated with certificate
     */
    @GetMapping("/{id}/orders")
    public List<OrderDto> getOrdersByPage(@PathVariable long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        return certificateFacade.findByCertificateId(id, page, size);
    }

    /**
     * handles POST requests for creating new certificate
     *
     * @param certificateDto representing new certificate to be created
     * @return certificate that was created
     */
    @PostMapping
    public CertificateDto create(@RequestBody CertificateDto certificateDto) {
        return certificateFacade.create(certificateDto);
    }

    /**
     * handles PATCH requests for updating name of specific certificate
     *
     * @param id ID of certificate to update
     * @param certificateDto with updated name
     * @return updated certificate
     */
    @PatchMapping("/{id}")
    public CertificateDto updateNameById(@PathVariable long id,
                                         @RequestBody CertificateDto certificateDto) {
        return certificateFacade.updateNameById(id, certificateDto);
    }

    /**
     * handles DELETE requests for deleting specific certificate
     *
     * @param id ID of certificate to delete
     * @return CustomMessageHolder object expressing that certificate was successfully deleted
     */
    @DeleteMapping("{id}")
    public CustomMessageHolder deleteById(@PathVariable long id) {
        return certificateFacade.deleteById(id);
    }
}
