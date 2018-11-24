package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "sender_image_url")
    private String senderImageURL;

    @Column(name = "jhi_read")
    private Boolean read;

    @ManyToOne
    @JsonIgnoreProperties("sentNotifications")
    private UserInfo sender;

    @ManyToOne
    @JsonIgnoreProperties("receivedNotifications")
    private UserInfo receiver;

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Notification timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Notification message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderImageURL() {
        return senderImageURL;
    }

    public Notification senderImageURL(String senderImageURL) {
        this.senderImageURL = senderImageURL;
        return this;
    }

    public void setSenderImageURL(String senderImageURL) {
        this.senderImageURL = senderImageURL;
    }

    public Boolean isRead() {
        return read;
    }

    public Notification read(Boolean read) {
        this.read = read;
        return this;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public UserInfo getSender() {
        return sender;
    }

    public Notification sender(UserInfo userInfo) {
        this.sender = userInfo;
        return this;
    }

    public void setSender(UserInfo userInfo) {
        this.sender = userInfo;
    }

    public UserInfo getReceiver() {
        return receiver;
    }

    public Notification receiver(UserInfo userInfo) {
        this.receiver = userInfo;
        return this;
    }

    public void setReceiver(UserInfo userInfo) {
        this.receiver = userInfo;
    }

    public Booking getBooking() {
        return booking;
    }

    public Notification booking(Booking booking) {
        this.booking = booking;
        return this;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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
        Notification notification = (Notification) o;
        if (notification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", message='" + getMessage() + "'" +
            ", senderImageURL='" + getSenderImageURL() + "'" +
            ", read='" + isRead() + "'" +
            "}";
    }
}
