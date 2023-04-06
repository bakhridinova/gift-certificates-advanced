package com.epam.esm.contoller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomTagValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;
    private final HateoasAdder<TagDTO> tagHateoasAdder;
    private final HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    /**
     * GET endpoint to retrieve list of tags
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of TagDTO containing tag details
     */
    @GetMapping
    public List<TagDTO> getAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<TagDTO> tags = tagService.findAll(pagination);
        tagHateoasAdder.addLinksToEntityList(tags);
        return tags;
    }

    /**
     * GET endpoint to retrieve specific tag by its ID
     *
     * @param id Long ID of required tag
     * @return CertificateDTO containing details of specified tag
     */
    @GetMapping("/{id}")
    public TagDTO getById(@PathVariable Long id) {
        CustomValidator.validateId(id);

        TagDTO tag = tagService.findById(id);
        tagHateoasAdder.addLinksToEntity(tag);
        return tag;
    }


    /**
     * GET endpoint to retrieve the most commonly used tag of a user
     * with the highest cost of all orders
     *
     * @return TagDTO containing details of specified tag
     */
    @GetMapping("/top")
    public TagDTO getTag() {
        TagDTO tag = tagService.findSpecial();
        tagHateoasAdder.addLinksToEntity(tag);
        return tag;
    }

    /**
     * handles POST requests for creating new tag
     *
     * @param tagDTO TagDTO object representing new tag to be created
     * @return TagDTO object that was created
     */
    @PostMapping
    public TagDTO post(@RequestBody TagDTO tagDTO) {
        CustomTagValidator.validate(tagDTO);

        TagDTO tag = tagService.create(tagDTO);
        tagHateoasAdder.addLinksToEntity(tag);
        return tag;
    }

    /**
     * handles DELETE requests for deleting specific tag
     *
     * @param id ID of tag to delete
     * @return CustomResponseBody object expressing that tag was successfully deleted
     */
    @DeleteMapping("/{id}")
    public CustomMessageHolder delete(@PathVariable long id) {
        CustomValidator.validateId(id);

        tagService.delete(id);
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.OK, "tag was successfully deleted");
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return messageHolder;
    }
}
