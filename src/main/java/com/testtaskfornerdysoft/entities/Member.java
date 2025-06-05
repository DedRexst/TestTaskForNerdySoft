package com.testtaskfornerdysoft.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member extends BaseEntity {
    @NotBlank(message = "Name can't be blank")
    @Column(name = "name")
    private String name;
    @CreationTimestamp
    @Column(name = "membership-date")
    private LocalDateTime membershipDate;
}