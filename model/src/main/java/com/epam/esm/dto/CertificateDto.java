package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * class representing data transfer object for certificate
 *
 * @author bakhridinova
 */


@Data
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDto extends RepresentationModel<CertificateDto> {
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer duration;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    private Set<TagDto> tags;
}
