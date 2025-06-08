package com.testtaskfornerdysoft.services.impl;

import com.testtaskfornerdysoft.dtos.requests.PatchMemberDto;
import com.testtaskfornerdysoft.dtos.requests.PostMemberDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberByIdDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberDto;
import com.testtaskfornerdysoft.entities.Book;
import com.testtaskfornerdysoft.entities.Member;
import com.testtaskfornerdysoft.mappers.MembersMapper;
import com.testtaskfornerdysoft.repositories.BooksRepository;
import com.testtaskfornerdysoft.repositories.MembersRepository;
import com.testtaskfornerdysoft.services.MembersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {
    private final MembersRepository membersRepository;
    private final BooksRepository booksRepository;
    private final MembersMapper membersMapper;
    @Value("${MAX_AMOUNT_OF_BOOKS_PER_MEMBER}")
    private int maxAmountOfBooksPerMember;

    @Override
    @Transactional
    public GetMemberDto postMember(PostMemberDto postMemberDto) {
        return membersMapper.entityToDto(membersRepository.save(membersMapper.dtoToEntity(postMemberDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetMemberDto> getMembers(Pageable pageable) {
        return membersRepository.findAll(pageable).map(membersMapper::entityToDto);
    }

    @Override
    @Transactional
    public void patchMember(@Valid PatchMemberDto patchMemberDto) {
        Member member = membersRepository.findById(patchMemberDto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with this id don't exists"));
        membersMapper.updateEntityFromDto(patchMemberDto, member);
    }

    @Override
    @Transactional(readOnly = true)
    public GetMemberByIdDto getMember(UUID id) {
        Member member = membersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with this id don't exists"));
        Hibernate.initialize(member.getBooks());
        return membersMapper.entityToDtoId(member);
    }

    @Override
    @Transactional
    public void deleteMember(UUID id) {
        Member member = membersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with this id don't exists"));

        if (member.getBooks().isEmpty()) {
            membersRepository.delete(member);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You cannot remove a member who has borrowed books");
        }
    }

    @Override
    @Transactional
    public void burrowTheBook(UUID bookId, UUID memberId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id don't exists"));
        Member member = membersRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with this id don't exists"));
        if (maxAmountOfBooksPerMember - member.getBooks().size() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member borrowed to many books");
        } else if (book.getAmount() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The book is out of stock");
        } else if (book.getMembers().contains(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member already have the book");
        } else {
            book.getMembers().add(member);
            book.setAmount(book.getAmount() - 1);
            member.getBooks().add(book);
        }
    }

    @Override
    @Transactional
    public void returnTheBook(UUID bookId, UUID memberId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id don't exists"));
        Member member = membersRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with this id don't exists"));
        if (!book.getMembers().contains(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member does not have this book");
        } else {
            book.getMembers().remove(member);
            book.setAmount(book.getAmount() + 1);
            member.getBooks().remove(book);
        }
    }


}
