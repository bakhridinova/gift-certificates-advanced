package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * class representing data transfer object for tag
 *
 * @author bakhridinova
 */


@Data
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto extends RepresentationModel<TagDto> {
    private Long id;

    private String name;
}
