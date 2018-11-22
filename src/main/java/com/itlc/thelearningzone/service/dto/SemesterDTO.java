package com.itlc.thelearningzone.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.itlc.thelearningzone.domain.enumeration.SemesterNumber;

/**
 * A DTO for the Semester entity.
 */
public class SemesterDTO implements Serializable {

    private Long id;

    @NotNull
    private SemesterNumber semesterNumber;

    private LocalDate semesterStartDate;

    private LocalDate semesterEndDate;

    private Set<SubjectDTO> subjects = new HashSet<>();

    private Set<UserInfoDTO> userInfos = new HashSet<>();

    private Long courseYearId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SemesterNumber getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(SemesterNumber semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public LocalDate getSemesterStartDate() {
        return semesterStartDate;
    }

    public void setSemesterStartDate(LocalDate semesterStartDate) {
        this.semesterStartDate = semesterStartDate;
    }

    public LocalDate getSemesterEndDate() {
        return semesterEndDate;
    }

    public void setSemesterEndDate(LocalDate semesterEndDate) {
        this.semesterEndDate = semesterEndDate;
    }

    public Set<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Set<UserInfoDTO> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(Set<UserInfoDTO> userInfos) {
        this.userInfos = userInfos;
    }

    public Long getCourseYearId() {
        return courseYearId;
    }

    public void setCourseYearId(Long courseYearId) {
        this.courseYearId = courseYearId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SemesterDTO semesterDTO = (SemesterDTO) o;
        if (semesterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), semesterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SemesterDTO{" +
            "id=" + getId() +
            ", semesterNumber='" + getSemesterNumber() + "'" +
            ", semesterStartDate='" + getSemesterStartDate() + "'" +
            ", semesterEndDate='" + getSemesterEndDate() + "'" +
            ", courseYear=" + getCourseYearId() +
            "}";
    }
}
