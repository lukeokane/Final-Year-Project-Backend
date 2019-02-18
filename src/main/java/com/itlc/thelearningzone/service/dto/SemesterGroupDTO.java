package com.itlc.thelearningzone.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the SemesterGroup entity.
 */
public class SemesterGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Set<SubjectDTO> subjects = new HashSet<>();

    private Long semesterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SemesterGroupDTO semesterGroupDTO = (SemesterGroupDTO) o;
        if (semesterGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), semesterGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SemesterGroupDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", semester=" + getSemesterId() +
            "}";
    }
}
