package com.testtaskfornerdysoft.controllers;

import com.testtaskfornerdysoft.dtos.requests.PostAuthorDto;
import com.testtaskfornerdysoft.dtos.responses.GetAuthorDto;
import com.testtaskfornerdysoft.services.AuthorsService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorsService authorsService;

    @PostMapping
    public GetAuthorDto postAuthor(@RequestBody @Valid PostAuthorDto postAuthorDto) {
        return authorsService.postAuthor(postAuthorDto);
    }

    @GetMapping
    public Page<GetAuthorDto> getAuthors(Pageable pageable) {
        return authorsService.getAuthors(pageable);
    }

    @GetMapping("/by-id")
    public GetAuthorDto getAuthor(@PathParam("id") UUID id) {
        return authorsService.getAuthor(id);
    }

    @DeleteMapping
    public void deleteAuthor(@PathParam("id") UUID id){
        authorsService.deleteAuthor(id);
    }
}
