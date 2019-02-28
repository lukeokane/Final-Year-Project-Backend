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
    private Set<UserInfo> userInfos = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "course_year_subject",
               joinColumns = @JoinColumn(name = "course_years_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "subjects_id", referencedColumnName = "id"))
    private Set<Subject> subjects = new HashSet<>();

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

    public Set<UserInfo> getUserInfos() {
        return userInfos;
    }

    public CourseYear userInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
        return this;
    }

    public CourseYear addUserInfo(UserInfo userInfo) {
        this.userInfos.add(userInfo);
        userInfo.setCourseYear(this);
        return this;
    }

    public CourseYear removeUserInfo(UserInfo userInfo) {
        this.userInfos.remove(userInfo);
        userInfo.setCourseYear(null);
        return this;
    }

    public void setUserInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public CourseYear subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public CourseYear addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getCourseYears().add(this);
        return this;
    }

    public CourseYear removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getCourseYears().remove(this);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
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
