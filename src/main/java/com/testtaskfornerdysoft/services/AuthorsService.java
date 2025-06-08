package com.testtaskfornerdysoft.services;

import com.testtaskfornerdysoft.dtos.requests.PostAuthorDto;
import com.testtaskfornerdysoft.dtos.responses.GetAuthorDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuthorsService {
     GetAuthorDto postAuthor(@Valid PostAuthorDto postMemberDto);

     Page<GetAuthorDto> getAuthors(Pageable pageable);

     GetAuthorDto getAuthor(UUID id);

     void deleteAuthor(UUID id);
}
