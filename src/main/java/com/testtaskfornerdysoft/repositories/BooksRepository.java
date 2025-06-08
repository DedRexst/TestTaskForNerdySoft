package com.testtaskfornerdysoft.repositories;

import com.testtaskfornerdysoft.dtos.requests.PatchMemberDto;
import com.testtaskfornerdysoft.dtos.responses.BorrowedBookStats;
import com.testtaskfornerdysoft.entities.Book;
import com.testtaskfornerdysoft.entities.Member;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Book, UUID> {
    Page<Book> findAllByMembers(Member member, Pageable pageable);

    @Query("SELECT DISTINCT b.book FROM BorrowedBook b")
    Page<Book> findDistinctBorrowedBookNames(Pageable pageable);

    @Query("""
            SELECT b.title AS title, COUNT(bb) AS borrowedCount
            FROM BorrowedBook bb
            JOIN bb.book b
            GROUP BY b.title
            """)
    Page<BorrowedBookStats> findBorrowedBookStats(Pageable pageable);

    Optional<Book> findByTitle(String title);

}
