package com.testtaskfornerdysoft.services;

import com.testtaskfornerdysoft.dtos.requests.PatchMemberDto;
import com.testtaskfornerdysoft.dtos.requests.PostMemberDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberByIdDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberDto;
import com.testtaskfornerdysoft.entities.Member;
import com.testtaskfornerdysoft.mappers.MembersMapper;
import com.testtaskfornerdysoft.repositories.BooksRepository;
import com.testtaskfornerdysoft.repositories.MembersRepository;
import com.testtaskfornerdysoft.services.impl.MembersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MembersServiceImplTest {

    @Mock
    private MembersRepository membersRepository;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private MembersMapper membersMapper;

    @InjectMocks
    private MembersServiceImpl membersService;

    @BeforeEach
    void setUp() {
        // Simulate the @Value injection
        membersService = new MembersServiceImpl(membersRepository, booksRepository, membersMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(membersService, "maxAmountOfBooksPerMember", 5);
    }

    @Test
    void postMember_shouldSaveAndReturnDto() {
        PostMemberDto postDto = new PostMemberDto("Alice");
        Member savedMember = new Member();
        GetMemberDto expectedDto = new GetMemberDto(UUID.randomUUID(), "Alice", LocalDateTime.now());

        when(membersMapper.dtoToEntity(postDto)).thenReturn(savedMember);
        when(membersRepository.save(savedMember)).thenReturn(savedMember);
        when(membersMapper.entityToDto(savedMember)).thenReturn(expectedDto);

        GetMemberDto result = membersService.postMember(postDto);

        assertThat(result).isEqualTo(expectedDto);
        verify(membersRepository).save(savedMember);
    }

    @Test
    void getMembers_shouldReturnPagedDtos() {
        Pageable pageable = Pageable.ofSize(10);
        Member member = new Member();
        GetMemberDto dto = new GetMemberDto(UUID.randomUUID(), "Alice", LocalDateTime.now());
        Page<Member> members = new PageImpl<>(List.of(member));

        when(membersRepository.findAll(pageable)).thenReturn(members);
        when(membersMapper.entityToDto(member)).thenReturn(dto);

        Page<GetMemberDto> result = membersService.getMembers(pageable);

        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void patchMember_shouldUpdateMember() {
        UUID id = UUID.randomUUID();
        PatchMemberDto patchDto = new PatchMemberDto(id, "New Name");
        Member existing = new Member();

        when(membersRepository.findById(id)).thenReturn(Optional.of(existing));

        membersService.patchMember(patchDto);

        verify(membersMapper).updateEntityFromDto(patchDto, existing);
    }

    @Test
    void patchMember_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        PatchMemberDto patchDto = new PatchMemberDto(id, "Name");

        when(membersRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> membersService.patchMember(patchDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Member with this id don't exists");
    }

    @Test
    void getMember_shouldReturnDto() {
        UUID id = UUID.randomUUID();
        Member member = new Member();
        member.setBooks(new HashSet<>());
        GetMemberByIdDto dto = new GetMemberByIdDto(id, "Alice", LocalDateTime.now(), Set.of());

        when(membersRepository.findById(id)).thenReturn(Optional.of(member));
        when(membersMapper.entityToDtoId(member)).thenReturn(dto);

        GetMemberByIdDto result = membersService.getMember(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getMember_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(membersRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> membersService.getMember(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Member with this id don't exists");
    }

    @Test
    void deleteMember_shouldDeleteIfNoBooks() {
        UUID id = UUID.randomUUID();
        Member member = new Member();
        member.setBooks(new HashSet<>());

        when(membersRepository.findById(id)).thenReturn(Optional.of(member));

        membersService.deleteMember(id);

        verify(membersRepository).delete(member);
    }

    @Test
    void deleteMember_shouldThrowIfHasBooks() {
        UUID id = UUID.randomUUID();
        Member member = new Member();
        member.setBooks(Set.of(mock(com.testtaskfornerdysoft.entities.Book.class)));

        when(membersRepository.findById(id)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> membersService.deleteMember(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("You cannot remove a member who has borrowed books");
    }

    @Test
    void deleteMember_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(membersRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> membersService.deleteMember(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Member with this id don't exists");
    }
}
