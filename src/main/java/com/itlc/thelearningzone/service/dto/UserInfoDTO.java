package com.itlc.thelearningzone.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserInfo entity.
 */
public class UserInfoDTO implements Serializable {

    private Long id;

    private String tutorSkills;

    private String profileImageURL;

    private Long userId;

    private Long semesterGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTutorSkills() {
        return tutorSkills;
    }

    public void setTutorSkills(String tutorSkills) {
        this.tutorSkills = tutorSkills;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSemesterGroupId() {
        return semesterGroupId;
    }

    public void setSemesterGroupId(Long semesterGroupId) {
        this.semesterGroupId = semesterGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserInfoDTO userInfoDTO = (UserInfoDTO) o;
        if (userInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
            "id=" + getId() +
            ", tutorSkills='" + getTutorSkills() + "'" +
            ", profileImageURL='" + getProfileImageURL() + "'" +
            ", user=" + getUserId() +
            ", semesterGroup=" + getSemesterGroupId() +
            "}";
    }
}
