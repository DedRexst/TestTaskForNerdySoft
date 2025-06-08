package com.testtaskfornerdysoft.services;

import com.testtaskfornerdysoft.dtos.requests.PatchMemberDto;
import com.testtaskfornerdysoft.dtos.requests.PostMemberDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberByIdDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MembersService {
    GetMemberDto postMember(PostMemberDto postMemberDto);

    Page<GetMemberDto> getMembers(Pageable pageable);

    void patchMember(PatchMemberDto patchMemberDto);

    GetMemberByIdDto getMember(UUID id);

    void deleteMember(UUID id);

    void burrowTheBook(UUID bookId, UUID memberId);

    void returnTheBook(UUID bookId, UUID memberId);

}
