package com.epam.esm.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomMessageHolder extends RepresentationModel<CustomMessageHolder> {
    private final HttpStatus status;
    private final String message;

    public CustomMessageHolder(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
