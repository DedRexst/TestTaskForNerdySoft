package com.testtaskfornerdysoft.controllers;

import com.testtaskfornerdysoft.dtos.requests.PatchBookDto;
import com.testtaskfornerdysoft.dtos.requests.PostBookDto;
import com.testtaskfornerdysoft.dtos.responses.BorrowedBookStats;
import com.testtaskfornerdysoft.dtos.responses.GetBookDto;
import com.testtaskfornerdysoft.services.BooksService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BooksService booksService;

    @PostMapping
    public GetBookDto postTheBook(@RequestBody PostBookDto postBookDto) {
        return booksService.postTheBook(postBookDto);
    }

    @GetMapping
    public Page<GetBookDto> getBooks(Pageable pageable){
        return booksService.getBooks(pageable);
    }
    @GetMapping("/by-id")
    public GetBookDto getBook(@PathParam("id") UUID id){
        return booksService.getBook(id);
    }
    @GetMapping("/retrieve-all-books/by-user-name")
    public Page<GetBookDto> retrieveAllBooksByUserName(Pageable pageable, @RequestParam String name) {
        return booksService.retrieveAllBooksByUserName(name, pageable);
    }

    @GetMapping("/retrieve-all-books/that-was-borrowed")
    public Page<GetBookDto> retrieveAllBooksThatWasBorrowed(Pageable pageable) {
        return booksService.retrieveAllBooksThatWasBorrowed(pageable);
    }

    @GetMapping("/retrieve-all-books/that-was-borrowed-and-how-many")
    public Page<BorrowedBookStats> retrieveAllBooksThatWasBorrowedAndHowMany(Pageable pageable) {
        return booksService.retrieveAllBooksThatWasBorrowedAndHowMany(pageable);
    }

    @DeleteMapping
    public void deleteBook(@PathParam("id") UUID id){
        booksService.deleteBook(id);
    }

    @PatchMapping
    public void patchBook(@RequestBody PatchBookDto patchBookDto){
        booksService.patchBook(patchBookDto);
    }
}
