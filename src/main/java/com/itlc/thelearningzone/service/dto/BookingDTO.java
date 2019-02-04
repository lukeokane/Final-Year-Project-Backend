package com.itlc.thelearningzone.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;

/**
 * A DTO for the Booking entity.
 */
public class BookingDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String requestedBy;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    private String userComments;

    @NotNull
    private OrdinalScale importanceLevel;

    private Integer adminAcceptedId;

    private Boolean tutorAccepted;

    private Integer tutorAcceptedId;

    private Instant modifiedTimestamp;

    private Integer tutorRejectedCount;

    private Boolean cancelled;

    private String requestTimes;

    private Long subjectId;

    private Set<UserInfoDTO> userInfos = new HashSet<>();
    
    private Set<BookingUserDetailsDTO> bookingUserDetailsDTO = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Set<BookingUserDetailsDTO> getBookingUserDetailsDTO() {
		return bookingUserDetailsDTO;
	}

	public void setBookingUserDetailsDTO(Set<BookingUserDetailsDTO> bookingUserDetailsDTO) {
		this.bookingUserDetailsDTO = bookingUserDetailsDTO;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public OrdinalScale getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(OrdinalScale importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    public Integer getAdminAcceptedId() {
        return adminAcceptedId;
    }

    public void setAdminAcceptedId(Integer adminAcceptedId) {
        this.adminAcceptedId = adminAcceptedId;
    }

    public Boolean isTutorAccepted() {
        return tutorAccepted;
    }

    public void setTutorAccepted(Boolean tutorAccepted) {
        this.tutorAccepted = tutorAccepted;
    }

    public Integer getTutorAcceptedId() {
        return tutorAcceptedId;
    }

    public void setTutorAcceptedId(Integer tutorAcceptedId) {
        this.tutorAcceptedId = tutorAcceptedId;
    }

    public Instant getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Instant modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public Integer getTutorRejectedCount() {
        return tutorRejectedCount;
    }

    public void setTutorRejectedCount(Integer tutorRejectedCount) {
        this.tutorRejectedCount = tutorRejectedCount;
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getRequestTimes() {
        return requestTimes;
    }

    public void setRequestTimes(String requestTimes) {
        this.requestTimes = requestTimes;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Set<UserInfoDTO> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(Set<UserInfoDTO> userInfos) {
        this.userInfos = userInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;
        if (bookingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", requestedBy='" + getRequestedBy() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", userComments='" + getUserComments() + "'" +
            ", importanceLevel='" + getImportanceLevel() + "'" +
            ", adminAcceptedId=" + getAdminAcceptedId() +
            ", tutorAccepted='" + isTutorAccepted() + "'" +
            ", tutorAcceptedId=" + getTutorAcceptedId() +
            ", modifiedTimestamp='" + getModifiedTimestamp() + "'" +
            ", tutorRejectedCount=" + getTutorRejectedCount() +
            ", cancelled='" + isCancelled() + "'" +
            ", requestTimes='" + getRequestTimes() + "'" +
            ", subject=" + getSubjectId() +
            "}";
    }
}
