package com.testtaskfornerdysoft.dtos.requests;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
public record PostMemberDto(@NotBlank(message = "Name can't be blank") String name) implements Serializable {
}