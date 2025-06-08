package com.testtaskfornerdysoft.controllers;


import com.testtaskfornerdysoft.dtos.requests.PatchMemberDto;
import com.testtaskfornerdysoft.dtos.requests.PostMemberDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberByIdDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberDto;
import com.testtaskfornerdysoft.services.MembersService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MembersController {
    private final MembersService membersService;
    @PostMapping
    public GetMemberDto postMember(@RequestBody @Valid PostMemberDto postMemberDto){
        return membersService.postMember(postMemberDto);
    }
    @GetMapping
    public Page<GetMemberDto> getMembers(Pageable pageable){
        return membersService.getMembers(pageable);
    }
    @GetMapping("/by-id")
    public GetMemberByIdDto getMember(@PathParam("id") UUID id) {
        return membersService.getMember(id);
    }
    @PatchMapping
    public void patchMember(@RequestBody PatchMemberDto patchMemberDto){
        membersService.patchMember(patchMemberDto);
    }
    @DeleteMapping
    public void deleteMember(@RequestParam("id") UUID id){
        membersService.deleteMember(id);
    }
    @PatchMapping("/borrow-the-book")
    public void burrowTheBook(@RequestParam UUID bookId, @RequestParam UUID memberId){
        membersService.burrowTheBook(bookId, memberId);
    }
    @PatchMapping("/return-the-book")
    public void returnTheBook(@RequestParam UUID bookId, @RequestParam UUID memberId){
        membersService.returnTheBook(bookId, memberId);
    }


}
