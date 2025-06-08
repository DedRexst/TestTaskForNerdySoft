package com.testtaskfornerdysoft.services.impl;

import com.testtaskfornerdysoft.dtos.requests.PostAuthorDto;
import com.testtaskfornerdysoft.dtos.responses.GetAuthorDto;
import com.testtaskfornerdysoft.entities.Author;
import com.testtaskfornerdysoft.entities.Book;
import com.testtaskfornerdysoft.mappers.AuthorsMapper;
import com.testtaskfornerdysoft.repositories.AuthorsRepository;
import com.testtaskfornerdysoft.services.AuthorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorsRepository authorsRepository;
    private final AuthorsMapper authorsMapper;

    @Override
    @Transactional
    public GetAuthorDto postAuthor(PostAuthorDto postMemberDto) {
        return authorsMapper.entityToDto(authorsRepository.save(authorsMapper.dtoToEntity(postMemberDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAuthorDto> getAuthors(Pageable pageable) {
        return authorsRepository.findAll(pageable).map(authorsMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAuthorDto getAuthor(UUID id) {
        return authorsMapper.entityToDto(authorsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author with this id don't exists")));
    }

    @Override
    @Transactional
    public void deleteAuthor(UUID id) {
        Author author = authorsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auhtor with this id don't exists"));
        if (author.getBooks().isEmpty()) {
            authorsRepository.delete(author);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You cannot author with book.");
        }
    }
}