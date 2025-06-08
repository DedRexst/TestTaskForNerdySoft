package com.testtaskfornerdysoft.dtos.responses;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link com.testtaskfornerdysoft.entities.Member}
 */
public record GetMemberByIdDto(UUID id, @NotBlank(message = "Name can't be blank") String name, LocalDateTime membershipDate,
                               Set<GetBookDto> books) implements Serializable {
}