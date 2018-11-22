package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.SemesterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Semester and its DTO SemesterDTO.
 */
@Mapper(componentModel = "spring", uses = {SubjectMapper.class, UserInfoMapper.class, CourseYearMapper.class})
public interface SemesterMapper extends EntityMapper<SemesterDTO, Semester> {

    @Mapping(source = "courseYear.id", target = "courseYearId")
    SemesterDTO toDto(Semester semester);

    @Mapping(source = "courseYearId", target = "courseYear")
    Semester toEntity(SemesterDTO semesterDTO);

    default Semester fromId(Long id) {
        if (id == null) {
            return null;
        }
        Semester semester = new Semester();
        semester.setId(id);
        return semester;
    }
}
