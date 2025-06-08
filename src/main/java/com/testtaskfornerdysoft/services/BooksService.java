package com.testtaskfornerdysoft.services;

import com.testtaskfornerdysoft.dtos.requests.PatchBookDto;
import com.testtaskfornerdysoft.dtos.requests.PostBookDto;
import com.testtaskfornerdysoft.dtos.responses.BorrowedBookStats;
import com.testtaskfornerdysoft.dtos.responses.GetBookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BooksService {
    Page<GetBookDto> retrieveAllBooksByUserName(String name, Pageable pageable);

    Page<GetBookDto> retrieveAllBooksThatWasBorrowed(Pageable pageable);

    Page<BorrowedBookStats> retrieveAllBooksThatWasBorrowedAndHowMany(Pageable pageable);

    GetBookDto postTheBook(PostBookDto postBookDto);

    Page<GetBookDto> getBooks(Pageable pageable);

    GetBookDto getBook(UUID id);

    void deleteBook(UUID id);

    void patchBook(PatchBookDto patchBookDto);
}
