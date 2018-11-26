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
 * A SemesterGroup.
 */
@Entity
@Table(name = "semester_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SemesterGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "semesterGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserInfo> userInfos = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("semesterGroups")
    private Semester semester;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public SemesterGroup title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<UserInfo> getUserInfos() {
        return userInfos;
    }

    public SemesterGroup userInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
        return this;
    }

    public SemesterGroup addUserInfo(UserInfo userInfo) {
        this.userInfos.add(userInfo);
        userInfo.setSemesterGroup(this);
        return this;
    }

    public SemesterGroup removeUserInfo(UserInfo userInfo) {
        this.userInfos.remove(userInfo);
        userInfo.setSemesterGroup(null);
        return this;
    }

    public void setUserInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public Semester getSemester() {
        return semester;
    }

    public SemesterGroup semester(Semester semester) {
        this.semester = semester;
        return this;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
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
        SemesterGroup semesterGroup = (SemesterGroup) o;
        if (semesterGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), semesterGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SemesterGroup{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
