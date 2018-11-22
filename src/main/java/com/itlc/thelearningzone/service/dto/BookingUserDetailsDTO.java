package com.itlc.thelearningzone.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;

/**
 * A DTO for the BookingUserDetails entity.
 */
public class BookingUserDetailsDTO implements Serializable {

    private Long id;

    private String userFeedback;

    private OrdinalScale userSatisfaction;

    private Instant usercheckInTime;

    private Instant usercheckOutTime;

    private Boolean userCancelled;

    private Boolean tutorRejected;

    private Long userInfoId;

    private Long bookingId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public OrdinalScale getUserSatisfaction() {
        return userSatisfaction;
    }

    public void setUserSatisfaction(OrdinalScale userSatisfaction) {
        this.userSatisfaction = userSatisfaction;
    }

    public Instant getUsercheckInTime() {
        return usercheckInTime;
    }

    public void setUsercheckInTime(Instant usercheckInTime) {
        this.usercheckInTime = usercheckInTime;
    }

    public Instant getUsercheckOutTime() {
        return usercheckOutTime;
    }

    public void setUsercheckOutTime(Instant usercheckOutTime) {
        this.usercheckOutTime = usercheckOutTime;
    }

    public Boolean isUserCancelled() {
        return userCancelled;
    }

    public void setUserCancelled(Boolean userCancelled) {
        this.userCancelled = userCancelled;
    }

    public Boolean isTutorRejected() {
        return tutorRejected;
    }

    public void setTutorRejected(Boolean tutorRejected) {
        this.tutorRejected = tutorRejected;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingUserDetailsDTO bookingUserDetailsDTO = (BookingUserDetailsDTO) o;
        if (bookingUserDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookingUserDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BookingUserDetailsDTO{" +
            "id=" + getId() +
            ", userFeedback='" + getUserFeedback() + "'" +
            ", userSatisfaction='" + getUserSatisfaction() + "'" +
            ", usercheckInTime='" + getUsercheckInTime() + "'" +
            ", usercheckOutTime='" + getUsercheckOutTime() + "'" +
            ", userCancelled='" + isUserCancelled() + "'" +
            ", tutorRejected='" + isTutorRejected() + "'" +
            ", userInfo=" + getUserInfoId() +
            ", booking=" + getBookingId() +
            "}";
    }
}
