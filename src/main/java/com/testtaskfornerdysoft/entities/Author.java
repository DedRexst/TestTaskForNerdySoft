package com.testtaskfornerdysoft.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "author")
public class Author extends BaseEntity {
    @NotBlank(message = "Name can't be blank")
    @Column(name = "name", unique = true)
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Name should contain two capital words with name" +
            " and surname and space between.")
    private String name;
    @OneToMany
    @JoinColumn(name = "author_id")
    private List<Book> books;
}