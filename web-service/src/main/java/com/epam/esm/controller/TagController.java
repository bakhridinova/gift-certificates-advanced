package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.facade.TagFacade;
import lombok.RequiredArgsConstructor;
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
    private final TagFacade tagFacade;

    /**
     * GET endpoint to retrieve list of tags
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of tags
     */
    @GetMapping
    public List<TagDto> getAllByPage(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
       return tagFacade.findAllByPage(page, size);
    }

    /**
     * GET endpoint to retrieve specific tag by its ID
     *
     * @param id long ID of required tag
     * @return specified tag
     */
    @GetMapping("/{id}")
    public TagDto getById(@PathVariable long id) {
        return tagFacade.findById(id);
    }


    /**
     * GET endpoint to retrieve the most commonly used tag of a user
     * with the highest cost of all orders
     *
     * @return specified tag
     */
    @GetMapping("/special")
    public TagDto getSpecial() {
        return tagFacade.findSpecial();
    }

    /**
     * handles POST requests for creating new tag
     *
     * @param tagDto representing new tag to be created
     * @return tag that was created
     */
    @PostMapping
    public TagDto create(@RequestBody TagDto tagDto) {
        return tagFacade.create(tagDto);
    }

    /**
     * handles DELETE requests for deleting specific tag
     *
     * @param id ID of tag to delete
     * @return CustomResponseBody object expressing that tag was successfully deleted
     */
    @DeleteMapping("/{id}")
    public CustomMessageHolder deleteById(@PathVariable long id) {
        return tagFacade.deleteById(id);
    }
}
