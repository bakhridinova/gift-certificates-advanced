package com.epam.esm.contoller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
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
    private final HateoasAdder<CertificateDto> certificateHateoasAdder;
    private final OrderService orderService;
    private final HateoasAdder<OrderDto> orderHateoasAdder;
    private final HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    /**
     * GET endpoint to retrieve list of certificates
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of certificates
     */
    @GetMapping
    public List<CertificateDto> getAll(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<CertificateDto> certificates = certificateService.findAll(pagination);
        certificateHateoasAdder.addLinksToEntityList(certificates);
        return certificates;
    }

    /**
     * GET endpoint to retrieve specific certificate by its ID
     *
     * @param id long ID of required certificate
     * @return specified certificate
     */
    @GetMapping("/{id}")
    public CertificateDto getById(@PathVariable long id) {
        CustomValidator.validateId(id);

        CertificateDto certificate = certificateService.findById(id);
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
     * @return List of certificates based on provided search parameters
     */
    @GetMapping("/search")
    public List<CertificateDto> search(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "") String name,
                                       @RequestParam(defaultValue = "") String description,
                                       @RequestParam(defaultValue = "id") String sortType,
                                       @RequestParam(defaultValue = "asc") String sortOrder,
                                       @RequestBody(required = false) Set<TagDto> tags) {
        CustomPaginationValidator.validate(page, size);
        CustomSortValidator.validate(sortType, sortOrder);

        SearchFilter searchFilter = SearchFilter.builder()
                .pagination(new Pagination(page, size))
                .name(name).description(description)
                .sortType(sortType).sortOrder(sortOrder)
                .tagDtos(tags).build();

        List<CertificateDto> certificates = certificateService.findByFilter(searchFilter);
        certificateHateoasAdder.addLinksToEntityList(certificates);
        return certificates;
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
    public List<OrderDto> getOrders(@PathVariable long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        CustomValidator.validateId(id);
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<OrderDto> orders = orderService.findByCertificateId(id, pagination);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }

    /**
     * handles POST requests for creating new certificate
     *
     * @param certificateDto representing new certificate to be created
     * @return certificate that was created
     */
    @PostMapping
    public CertificateDto post(@RequestBody CertificateDto certificateDto) {
        CustomCertificateValidator.validate(certificateDto);

        CertificateDto certificate = certificateService.create(certificateDto);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    /**
     * handles PATCH requests for updating name of specific certificate
     *
     * @param id ID of certificate to update
     * @param certificateDto with updated name
     * @return updated certificate
     */
    @PatchMapping("/{id}")
    public CertificateDto patch(@PathVariable long id,
                                @RequestBody CertificateDto certificateDto) {
        CustomValidator.validateId(id);
        CustomCertificateValidator.validateName(certificateDto.getName());

        CertificateDto certificate = certificateService.updateName(id, certificateDto);
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
    public CustomMessageHolder delete(@PathVariable long id) {
        CustomValidator.validateId(id);

        certificateService.delete(id);
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.OK, "certificate was successfully deleted");
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return messageHolder;
    }
}
