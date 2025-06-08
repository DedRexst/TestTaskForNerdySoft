package com.testtaskfornerdysoft.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class BorrowedBookId implements Serializable {
    private static final long serialVersionUID = 740308162322724117L;
    @NotNull
    @Column(name = "BOOK_ID", nullable = false)
    private UUID bookId;

    @NotNull
    @Column(name = "MEMBER_ID", nullable = false)
    private UUID memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BorrowedBookId entity = (BorrowedBookId) o;
        return Objects.equals(this.bookId, entity.bookId) &&
                Objects.equals(this.memberId, entity.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, memberId);
    }

}