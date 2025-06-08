package com.testtaskfornerdysoft.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;
public record PatchMemberDto(@NotNull UUID id,
                             @NotBlank(message = "Name can't be blank") String name) implements Serializable {
}