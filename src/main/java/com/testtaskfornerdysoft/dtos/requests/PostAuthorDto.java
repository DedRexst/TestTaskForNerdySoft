package com.testtaskfornerdysoft.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.testtaskfornerdysoft.entities.Author}
 */
public record PostAuthorDto(
        @Pattern(message = "Name should contain two capital words with name and surname and space between.", regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$") @NotBlank(message = "Name can't be blank") String name) implements Serializable {
}