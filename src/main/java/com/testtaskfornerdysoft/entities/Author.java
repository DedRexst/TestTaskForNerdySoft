package com.testtaskfornerdysoft.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "author")
public class Author extends BaseEntity {
    @NotBlank(message = "Name can't be blank")
    @Column(name = "name")
    private String name;
}