package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CourseYear.
 */
@Entity
@Table(name = "course_year")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseYear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "course_year", nullable = false)
    private Integer courseYear;

    @OneToMany(mappedBy = "courseYear")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Semester> semesters = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("courseYears")
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCourseYear() {
        return courseYear;
    }

    public CourseYear courseYear(Integer courseYear) {
        this.courseYear = courseYear;
        return this;
    }

    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }

    public Set<Semester> getSemesters() {
        return semesters;
    }

    public CourseYear semesters(Set<Semester> semesters) {
        this.semesters = semesters;
        return this;
    }

    public CourseYear addSemester(Semester semester) {
        this.semesters.add(semester);
        semester.setCourseYear(this);
        return this;
    }

    public CourseYear removeSemester(Semester semester) {
        this.semesters.remove(semester);
        semester.setCourseYear(null);
        return this;
    }

    public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
    }

    public Course getCourse() {
        return course;
    }

    public CourseYear course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseYear courseYear = (CourseYear) o;
        if (courseYear.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseYear.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseYear{" +
            "id=" + getId() +
            ", courseYear=" + getCourseYear() +
            "}";
    }
}
