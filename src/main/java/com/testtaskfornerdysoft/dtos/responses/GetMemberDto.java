package com.testtaskfornerdysoft.dtos.responses;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
public record GetMemberDto(UUID id, String name, LocalDateTime membershipDate) implements Serializable {
}