package com.testtaskfornerdysoft.repositories;

import com.testtaskfornerdysoft.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembersRepository extends JpaRepository<Member, UUID> {
    @Modifying
    @Query("delete from BorrowedBook ur where ur.member.id = :memberId")
    void deleteUserRolesByUserId(@Param("memberId") UUID memberId);
    Optional<Member> findByName(String name);

}
