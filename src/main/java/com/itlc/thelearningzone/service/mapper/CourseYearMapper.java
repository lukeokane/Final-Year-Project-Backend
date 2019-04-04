package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.CourseYearDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CourseYear and its DTO CourseYearDTO.
 */
@Mapper(componentModel = "spring", uses = {SubjectMapper.class, CourseMapper.class})
public interface CourseYearMapper extends EntityMapper<CourseYearDTO, CourseYear> {

    @Mapping(source = "course.id", target = "courseId")
    CourseYearDTO toDto(CourseYear courseYear);

    @Mapping(target = "userInfos", ignore = true)
    @Mapping(source = "courseId", target = "course")
    CourseYear toEntity(CourseYearDTO courseYearDTO);

    default CourseYear fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseYear courseYear = new CourseYear();
        courseYear.setId(id);
        return courseYear;
    }
}
