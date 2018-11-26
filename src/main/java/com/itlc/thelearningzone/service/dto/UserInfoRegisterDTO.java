package com.itlc.thelearningzone.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserInfo entity.
 */
public class UserInfoRegisterDTO implements Serializable {

    private UserDTO user;

    private String tutorSkills;

    private Long userId;

    private Long semesterId;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getTutorSkills() {
        return tutorSkills;
    }

    public void setTutorSkills(String tutorSkills) {
        this.tutorSkills = tutorSkills;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

        UserInfoRegisterDTO userInfoDTO = (UserInfoRegisterDTO) o;
        if (userInfoDTO.getUser() == null || getUser() == null) {
            return false;
        }
        return Objects.equals(getUser(), userInfoDTO.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUser());
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
            "user=" + getUser().toString() +
            ", tutorSkills='" + getTutorSkills() + "'" +
            ", user=" + getUserId() +
            ", semester=" + getSemesterId() +
            "}";
    }
}
