package com.testtaskfornerdysoft.dtos.responses;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.testtaskfornerdysoft.entities.Author}
 */
public record GetAuthorDto(UUID id, @NotBlank(message = "Name can't be blank") String name) implements Serializable {
}