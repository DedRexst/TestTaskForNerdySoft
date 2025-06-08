package com.testtaskfornerdysoft.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record PatchBookDto(UUID id,
                           @Size(message = "Title min length - 3 symbols", min = 3) @Pattern(message = "Title should start with a capital letter", regexp = "^[A-Z].*") String title,
                           AuthorDto author) implements Serializable {
    public record AuthorDto(UUID id) implements Serializable {
    }
}