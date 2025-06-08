package com.testtaskfornerdysoft.services.impl;

import com.testtaskfornerdysoft.dtos.requests.PatchBookDto;
import com.testtaskfornerdysoft.dtos.requests.PostBookDto;
import com.testtaskfornerdysoft.dtos.responses.BorrowedBookStats;
import com.testtaskfornerdysoft.dtos.responses.GetBookDto;
import com.testtaskfornerdysoft.entities.Author;
import com.testtaskfornerdysoft.entities.Book;
import com.testtaskfornerdysoft.mappers.AuthorsMapper;
import com.testtaskfornerdysoft.mappers.BooksMapper;
import com.testtaskfornerdysoft.repositories.AuthorsRepository;
import com.testtaskfornerdysoft.repositories.BooksRepository;
import com.testtaskfornerdysoft.repositories.MembersRepository;
import com.testtaskfornerdysoft.services.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;
    private final MembersRepository membersRepository;
    private final BooksMapper booksMapper;
    private final AuthorsRepository authorRepository;
    private final AuthorsMapper authorsMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<GetBookDto> retrieveAllBooksByUserName(String name, Pageable pageable) {

        return booksRepository.findAllByMembers(membersRepository.findByName(name)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with this name don't exists")), pageable)
                .map(booksMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetBookDto> retrieveAllBooksThatWasBorrowed(Pageable pageable) {
        return booksRepository.findDistinctBorrowedBookNames(pageable).map(booksMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BorrowedBookStats> retrieveAllBooksThatWasBorrowedAndHowMany(Pageable pageable) {
        return booksRepository.findBorrowedBookStats(pageable);
    }

    @Override
    @Transactional
    public GetBookDto postTheBook(PostBookDto postBookDto) {
        Optional<Book> bookO = booksRepository.findByTitle(postBookDto.title());
        if (bookO.isPresent() && bookO.get().getAuthor().getName().equals(postBookDto.author().name())) {
            bookO.get().setAmount(bookO.get().getAmount() + 1);
            return booksMapper.entityToDto(bookO.get());
        } else {
            Book book = booksMapper.dtoToEntity(postBookDto);
            Optional<Author> authorO = authorRepository.findByName(booksMapper.dtoToEntity(postBookDto)
                    .getAuthor().getName());
            if (authorO.isEmpty()) {
                Author author = authorRepository.save(authorsMapper.dtoToEntity(postBookDto.author()));
                book.setAuthor(author);
            } else {
                book.setAuthor(authorO.get());
            }
            book.setAmount(1);
            return booksMapper.entityToDto(booksRepository.save(book));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetBookDto> getBooks(Pageable pageable) {
        return booksRepository.findAll(pageable).map(booksMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GetBookDto getBook(UUID id) {
        return booksMapper.entityToDto(booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id don't exists")));
    }

    @Override
    @Transactional
    public void deleteBook(UUID id) {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id don't exists"));
        if (book.getMembers().isEmpty()) {
            booksRepository.delete(book);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You cannot delete a borrowed book.");
        }
    }

    @Override
    @Transactional
    public void patchBook(PatchBookDto patchBookDto) {
        Book book = booksRepository.findById(patchBookDto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id don't exists"));
        Author author = authorRepository.findById(patchBookDto.author().id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author with this id don't exists"));
        booksMapper.updateEntityFromDto(patchBookDto, book);
        book.setAuthor(author);
    }
}
