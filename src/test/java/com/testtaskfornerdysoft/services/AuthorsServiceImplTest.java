package com.testtaskfornerdysoft.services;

import com.testtaskfornerdysoft.dtos.requests.PostAuthorDto;
import com.testtaskfornerdysoft.dtos.responses.GetAuthorDto;
import com.testtaskfornerdysoft.entities.Author;
import com.testtaskfornerdysoft.entities.Book;
import com.testtaskfornerdysoft.mappers.AuthorsMapper;
import com.testtaskfornerdysoft.repositories.AuthorsRepository;
import com.testtaskfornerdysoft.services.impl.AuthorsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorsServiceImplTest {

    private AuthorsRepository authorsRepository;
    private AuthorsMapper authorsMapper;
    private AuthorsServiceImpl authorsService;

    @BeforeEach
    void setUp() {
        authorsRepository = mock(AuthorsRepository.class);
        authorsMapper = mock(AuthorsMapper.class);
        authorsService = new AuthorsServiceImpl(authorsRepository, authorsMapper);
    }

    @Test
    void postAuthor_shouldSaveAndReturnDto() {
        PostAuthorDto postDto = new PostAuthorDto("Rey Ostin");
        Author entity = new Author();
        GetAuthorDto responseDto = new GetAuthorDto(UUID.randomUUID(), "Rey Ostin");

        when(authorsMapper.dtoToEntity(postDto)).thenReturn(entity);
        when(authorsRepository.save(entity)).thenReturn(entity);
        when(authorsMapper.entityToDto(entity)).thenReturn(responseDto);

        GetAuthorDto result = authorsService.postAuthor(postDto);

        assertEquals(responseDto, result);
        verify(authorsRepository).save(entity);
    }

    @Test
    void getAuthors_shouldReturnPagedDtos() {
        Author author = new Author();
        GetAuthorDto dto = new GetAuthorDto(UUID.randomUUID(), "Rey Ostin");
        Page<Author> page = new PageImpl<>(List.of(author));
        Pageable pageable = Pageable.unpaged();

        when(authorsRepository.findAll(pageable)).thenReturn(page);
        when(authorsMapper.entityToDto(author)).thenReturn(dto);

        Page<GetAuthorDto> result = authorsService.getAuthors(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void getAuthor_shouldReturnDto_whenFound() {
        UUID id = UUID.randomUUID();
        Author author = new Author();
        GetAuthorDto dto = new GetAuthorDto(UUID.randomUUID(), "Rey Ostin");

        when(authorsRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorsMapper.entityToDto(author)).thenReturn(dto);

        GetAuthorDto result = authorsService.getAuthor(id);

        assertEquals(dto, result);
    }

    @Test
    void getAuthor_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(authorsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> authorsService.getAuthor(id));
    }

    @Test
    void deleteAuthor_shouldDelete_whenNoBooks() {
        UUID id = UUID.randomUUID();
        Author author = new Author();
        author.setBooks(new ArrayList<>());

        when(authorsRepository.findById(id)).thenReturn(Optional.of(author));

        authorsService.deleteAuthor(id);

        verify(authorsRepository).delete(author);
    }

    @Test
    void deleteAuthor_shouldThrow_whenHasBooks() {
        UUID id = UUID.randomUUID();
        Author author = new Author();
        author.setBooks(List.of(new Book()));

        when(authorsRepository.findById(id)).thenReturn(Optional.of(author));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authorsService.deleteAuthor(id));
        assertTrue(ex.getReason().contains("cannot author with book"));
    }

    @Test
    void deleteAuthor_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(authorsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> authorsService.deleteAuthor(id));
    }
}
