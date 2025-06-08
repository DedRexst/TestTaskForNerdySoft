package com.testtaskfornerdysoft.mappers;

import com.testtaskfornerdysoft.dtos.requests.PatchMemberDto;
import com.testtaskfornerdysoft.dtos.requests.PostMemberDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberByIdDto;
import com.testtaskfornerdysoft.dtos.responses.GetMemberDto;
import com.testtaskfornerdysoft.entities.Member;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MembersMapper {
    Member dtoToEntity(PostMemberDto postMemberDto);

    GetMemberDto entityToDto(Member member);
    GetMemberByIdDto entityToDtoId(Member member);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PatchMemberDto patchMemberDto, @MappingTarget Member member);
}
