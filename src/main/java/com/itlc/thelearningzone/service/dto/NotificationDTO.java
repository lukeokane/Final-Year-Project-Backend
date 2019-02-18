package com.itlc.thelearningzone.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Notification entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant timestamp;

    @NotNull
    private String message;

    private String senderImageURL;

    private Boolean read;

    private Long senderId;

    private Long receiverId;

    private Long bookingId;
    
    public Set<BookingDTO> getBookingDTO() {
		return bookingDTO;
	}

	public void setBookingDTO(Set<BookingDTO> bookingDTO) {
		this.bookingDTO = bookingDTO;
	}

	private Set<BookingDTO> bookingDTO = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderImageURL() {
        return senderImageURL;
    }

    public void setSenderImageURL(String senderImageURL) {
        this.senderImageURL = senderImageURL;
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long userInfoId) {
        this.senderId = userInfoId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long userInfoId) {
        this.receiverId = userInfoId;
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

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", message='" + getMessage() + "'" +
            ", senderImageURL='" + getSenderImageURL() + "'" +
            ", read='" + isRead() + "'" +
            ", sender=" + getSenderId() +
            ", receiver=" + getReceiverId() +
            ", booking=" + getBookingId() +
            "}";
    }
}
