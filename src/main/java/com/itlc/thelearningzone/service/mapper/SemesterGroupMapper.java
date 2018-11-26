package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.SemesterGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SemesterGroup and its DTO SemesterGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {SemesterMapper.class})
public interface SemesterGroupMapper extends EntityMapper<SemesterGroupDTO, SemesterGroup> {

    @Mapping(source = "semester.id", target = "semesterId")
    SemesterGroupDTO toDto(SemesterGroup semesterGroup);

    @Mapping(target = "userInfos", ignore = true)
    @Mapping(source = "semesterId", target = "semester")
    SemesterGroup toEntity(SemesterGroupDTO semesterGroupDTO);

    default SemesterGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        SemesterGroup semesterGroup = new SemesterGroup();
        semesterGroup.setId(id);
        return semesterGroup;
    }
}
