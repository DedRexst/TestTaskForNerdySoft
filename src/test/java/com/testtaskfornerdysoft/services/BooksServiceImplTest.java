package com.testtaskfornerdysoft.services;

import com.testtaskfornerdysoft.dtos.requests.PatchBookDto;
import com.testtaskfornerdysoft.dtos.responses.GetAuthorDto;
import com.testtaskfornerdysoft.dtos.responses.GetBookDto;
import com.testtaskfornerdysoft.entities.Author;
import com.testtaskfornerdysoft.entities.Book;
import com.testtaskfornerdysoft.entities.Member;
import com.testtaskfornerdysoft.mappers.AuthorsMapper;
import com.testtaskfornerdysoft.mappers.BooksMapper;
import com.testtaskfornerdysoft.repositories.AuthorsRepository;
import com.testtaskfornerdysoft.repositories.BooksRepository;
import com.testtaskfornerdysoft.repositories.MembersRepository;
import com.testtaskfornerdysoft.services.impl.BooksServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceImplTest {
    @Mock
    private BooksRepository booksRepository;
    @Mock
    private MembersRepository membersRepository;
    @Mock
    private BooksMapper booksMapper;
    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private AuthorsMapper authorsMapper;

    @InjectMocks
    private BooksServiceImpl booksService;
    @Test
    void testRetrieveAllBooksByUserName_UserExists() {
        String username = "alice";
        Book book1;
        book1 = new Book("Java 101", new Author("Nade Bilbo", Collections.emptyList()), 1, Collections.emptySet());
        Member member = new Member("Alice", LocalDateTime.now(),
                Set.of());
        when(membersRepository.findByName(username))
                .thenReturn(Optional.of(member));
        // Stub repository to return a list of books for this member


        Pageable pageable = PageRequest.of(0, 1);
        Page<Book> bookPage = new PageImpl<>(List.of(book1), pageable, 1);
        when(booksRepository.findAllByMembers(member, pageable))
                .thenReturn(bookPage);
        // Stub mapper to convert each Book to BookDTO
        GetBookDto dto1 = new GetBookDto(null, "Java 101", new GetAuthorDto(null,"Nade Bilbo"), 1);
        when(booksMapper.entityToDto(book1)).thenReturn(dto1);

        Page<GetBookDto> result = booksService.retrieveAllBooksByUserName(username, pageable);

        // Assert that the returned DTOs match expected
        assertEquals(List.of(dto1), result.getContent());
        // Verify that repository methods were called
        verify(membersRepository).findByName(username);
        verify(booksRepository).findAllByMembers(member, pageable);
    }

    @Test
    void testRetrieveAllBooksByUserName_UserNotFound() {
        String username = "bob";
        Pageable pageable = PageRequest.of(0, 1);

        when(membersRepository.findByName(username))
                .thenReturn(Optional.empty());
        // Expect a ResponseStatusException when user is not found
        assertThrows(ResponseStatusException.class, () -> {
            booksService.retrieveAllBooksByUserName(username, pageable);
        });
    }
    @Test
    void testGetBooks_ReturnsAllMappedBooks() {
        // Stub repository to return a list of books
        Book book = new Book("Java 101", new Author("Nade Bilbo", Collections.emptyList()), 1, Collections.emptySet());
        Pageable pageable = PageRequest.of(0, 1);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        when(booksRepository.findAll(pageable)).thenReturn(bookPage);
        // Stub mapper
        GetBookDto dto = new GetBookDto(null, "Java 101", new GetAuthorDto(null,"Nade Bilbo"), 1);
        when(booksMapper.entityToDto(book)).thenReturn(dto);

        Page<GetBookDto> result = booksService.getBooks(pageable);

        assertEquals(List.of(dto), result.getContent());
    }

    @Test
    void testGetBook_Found() {
        UUID bookId = UUID.randomUUID();
        Book book = new Book("Java 101", new Author("Nade Bilbo", Collections.emptyList()), 1, Collections.emptySet());
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        GetBookDto dto = new GetBookDto(null, "Java 101", new GetAuthorDto(null,"Nade Bilbo"), 1);
        when(booksMapper.entityToDto(book)).thenReturn(dto);

        GetBookDto result = booksService.getBook(bookId);

        assertEquals(dto, result);
    }

    @Test
    void testGetBook_NotFound() {
        UUID bookId = UUID.randomUUID();
        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            booksService.getBook(bookId);
        });
    }
    @Test
    void deleteBook_shouldDeleteBookWhenNotBorrowed() {
        UUID bookId = UUID.randomUUID();
        Book book = new Book();
        book.setMembers(Collections.emptySet());

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));

        booksService.deleteBook(bookId);

        verify(booksRepository).delete(book);
    }

    @Test
    void deleteBook_shouldThrowWhenBookIsBorrowed() {
        UUID bookId = UUID.randomUUID();
        Book book = new Book();
        Member member = new Member();
        book.setMembers(Set.of(member));

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                booksService.deleteBook(bookId));

        assertEquals("404 NOT_FOUND \"You cannot delete a borrowed book.\"", exception.getMessage());
        verify(booksRepository, never()).delete(any());
    }

    @Test
    void deleteBook_shouldThrowWhenBookNotFound() {
        UUID bookId = UUID.randomUUID();
        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                booksService.deleteBook(bookId));

        assertEquals("404 NOT_FOUND \"Book with this id don't exists\"", exception.getMessage());
    }

    @Test
    void patchBook_shouldUpdateBookCorrectly() {
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        PatchBookDto patchDto = new PatchBookDto(bookId, "Java", new PatchBookDto.AuthorDto(authorId));

        Book book = new Book();
        Author author = new Author();

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorsRepository.findById(authorId)).thenReturn(Optional.of(author));

        booksService.patchBook(patchDto);

        verify(booksMapper).updateEntityFromDto(patchDto, book);
        assertEquals(author, book.getAuthor());
    }

    @Test
    void patchBook_shouldThrowWhenBookNotFound() {
        UUID bookId = UUID.randomUUID();
        PatchBookDto patchDto = new PatchBookDto(bookId, "Java", new PatchBookDto.AuthorDto(UUID.randomUUID()));

        when(booksRepository.findById(bookId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                booksService.patchBook(patchDto));

        assertEquals("404 NOT_FOUND \"Book with this id don't exists\"", exception.getMessage());
    }

    @Test
    void patchBook_shouldThrowWhenAuthorNotFound() {
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        PatchBookDto patchDto = new PatchBookDto(bookId,"Java",  new PatchBookDto.AuthorDto(authorId));

        Book book = new Book();

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorsRepository.findById(authorId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                booksService.patchBook(patchDto));

        assertEquals("404 NOT_FOUND \"Author with this id don't exists\"", exception.getMessage());
    }
}