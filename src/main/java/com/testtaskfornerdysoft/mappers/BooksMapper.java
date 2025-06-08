package com.testtaskfornerdysoft.mappers;

import com.testtaskfornerdysoft.dtos.requests.PatchBookDto;
import com.testtaskfornerdysoft.dtos.requests.PostBookDto;
import com.testtaskfornerdysoft.dtos.responses.GetBookDto;
import com.testtaskfornerdysoft.entities.Book;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BooksMapper {
    GetBookDto entityToDto(Book book);

    Book dtoToEntity(PostBookDto postBookDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchBookDto patchBookDto, @MappingTarget Book book);
}
