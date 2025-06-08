package com.testtaskfornerdysoft.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.boot.json.JsonWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book extends BaseEntity {
        @Size(min = 3, message = "Title min length - 3 symbols")
        @Pattern(regexp = "^[A-Z].*", message = "Title should start with a capital letter")
        @NotBlank(message = "Title can't be blank")
        @Column(name = "title", nullable = false, unique = true)
        private String title;
        @ManyToOne
        @JoinColumn(name = "author_id")
        private Author author;
        @Column(name = "amount")
        private int amount;

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinTable(
                name = "borrowed_books",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "member_id")
        )
        private Set<Member> members = new HashSet<>();
}