package com.itlc.thelearningzone.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CourseYear entity.
 */
public class CourseYearDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer courseYear;

    private Set<SubjectDTO> subjects = new HashSet<>();

    private Long courseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }

    public Set<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseYearDTO courseYearDTO = (CourseYearDTO) o;
        if (courseYearDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseYearDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseYearDTO{" +
            "id=" + getId() +
            ", courseYear=" + getCourseYear() +
            ", course=" + getCourseId() +
            "}";
    }
}
