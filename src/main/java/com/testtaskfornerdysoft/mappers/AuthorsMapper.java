package com.testtaskfornerdysoft.mappers;

import com.testtaskfornerdysoft.dtos.requests.PostAuthorDto;
import com.testtaskfornerdysoft.dtos.requests.PostBookDto;
import com.testtaskfornerdysoft.dtos.responses.GetAuthorDto;
import com.testtaskfornerdysoft.entities.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorsMapper {
    Author dtoToEntity(PostAuthorDto author);
    Author dtoToEntity(PostBookDto.AuthorDto author);

    GetAuthorDto entityToDto(Author save);

}
