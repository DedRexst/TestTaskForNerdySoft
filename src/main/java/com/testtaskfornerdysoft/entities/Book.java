package com.testtaskfornerdysoft.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends BaseEntity {
        @Size(min = 3)
        @NotBlank(message = "Title can't be blank")
        @Column(name = "amount", nullable = false)
        private String title;
        @ManyToOne
        @JoinColumn(name = "author_id")
        private Author author;
        @Column(name = "amount")
        private int amount;
}