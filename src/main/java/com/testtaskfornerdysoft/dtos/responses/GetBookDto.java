package com.testtaskfornerdysoft.dtos.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.testtaskfornerdysoft.entities.Book}
 */
public record GetBookDto(UUID id, @Size(min = 3) @NotBlank(message = "Title can't be blank") String title,
                         GetAuthorDto author, int amount) implements Serializable {
}