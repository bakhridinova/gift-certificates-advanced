package com.epam.esm.contoller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.dto.extra.SearchFilter;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.CustomCertificateValidator;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomSortValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private final HateoasAdder<CertificateDTO> certificateHateoasAdder;
    private final OrderService orderService;
    private final HateoasAdder<OrderDTO> orderHateoasAdder;
    private final HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    /**
     * GET endpoint to retrieve list of certificates
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of CertificateDTO containing certificate details
     */
    @GetMapping
    public List<CertificateDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<CertificateDTO> certificates = certificateService.findAll(pagination);
        certificateHateoasAdder.addLinksToEntityList(certificates);
        return certificates;
    }

    /**
     * GET endpoint to retrieve specific certificate by its ID
     *
     * @param id Long ID of required certificate
     * @return CertificateDTO containing details of specified certificate
     */
    @GetMapping("/{id}")
    public CertificateDTO getById(@PathVariable Long id) {
        CustomValidator.validateId(id);

        CertificateDTO certificate = certificateService.findById(id);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    /**
     * GET endpoint to search for certificates based on search parameters
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @param name String part of name (default is empty String)
     * @param description String part of description (default is empty String)
     * @param sortType property to sort by (default is 'id')
     * @param sortOrder sort order ('asc' or 'desc', default is 'asc')
     * @param tags set of tags to filter by (optional)
     * @return List of CertificateDTO objects based on provided search parameters
     */
    @GetMapping("/search")
    public List<CertificateDTO> search(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "") String name,
                                       @RequestParam(defaultValue = "") String description,
                                       @RequestParam(defaultValue = "id") String sortType,
                                       @RequestParam(defaultValue = "asc") String sortOrder,
                                       @RequestBody(required = false) Set<TagDTO> tags) {
        CustomPaginationValidator.validate(page, size);
        CustomSortValidator.validate(sortType, sortOrder);

        SearchFilter searchFilter = SearchFilter.builder()
                .pagination(new Pagination(page, size))
                .name(name).description(description)
                .sortType(sortType).sortOrder(sortOrder)
                .tagDTOs(tags).build();

        List<CertificateDTO> certificates = certificateService.findByFilter(searchFilter);
        certificateHateoasAdder.addLinksToEntityList(certificates);
        return certificates;
    }

    /**
     * GET endpoint to search for retrieving orders associated with specific certificate
     *
     * @param id ID of certificate for which to retrieve orders
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of OrderDTO objects representing orders associated with certificate
     */
    @GetMapping("/{id}/orders")
    public List<OrderDTO> getOrders(@PathVariable Long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        CustomValidator.validateId(id);
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<OrderDTO> orders = orderService.findByCertificateId(id, pagination);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }

    /**
     * handles POST requests for creating new certificate
     *
     * @param certificateDTO CertificateDTO object representing new certificate to be created
     * @return CertificateDTO object that was created
     */
    @PostMapping
    public CertificateDTO post(@RequestBody CertificateDTO certificateDTO) {
        CustomCertificateValidator.validate(certificateDTO);

        CertificateDTO certificate = certificateService.create(certificateDTO);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    /**
     * handles PATCH requests for updating name of specific certificate
     *
     * @param id ID of certificate to update
     * @param certificateDTO CertificateDTO object with updated name
     * @return updated CertificateDTO object
     */
    @PatchMapping("/{id}/description")
    public CertificateDTO patchName(@PathVariable Long id,
                                    @RequestBody CertificateDTO certificateDTO) {
        CustomValidator.validateId(id);
        String name = certificateDTO.getName();
        CustomCertificateValidator.validateName(name);

        CertificateDTO certificate = certificateService.updateName(id, certificateDTO);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    /**
     * handles DELETE requests for deleting specific certificate
     *
     * @param id ID of certificate to delete
     * @return CustomMessageHolder object expressing that certificate was successfully deleted
     */
    @DeleteMapping("{id}")
    public CustomMessageHolder delete(@PathVariable Long id) {
        CustomValidator.validateId(id);

        certificateService.delete(id);
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.OK, "certificate was successfully deleted");
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return messageHolder;
    }
}
