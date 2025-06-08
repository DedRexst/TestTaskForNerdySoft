package com.testtaskfornerdysoft.dtos.requests;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public record PostBookDto(@Size(min = 3)
                          @NotBlank(message = "Title can't be blank")
                          String title,
                          @NotNull(message = "Author can't be null")
                          AuthorDto author) implements Serializable {
    public record AuthorDto(@NotEmpty @NotBlank(message = "Name can't be blank") String name) implements Serializable {
    }
}