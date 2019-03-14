package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "requested_by", nullable = false)
    private String requestedBy;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "user_comments")
    private String userComments;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "importance_level", nullable = false)
    private OrdinalScale importanceLevel;

    @Column(name = "admin_accepted_id")
    private Integer adminAcceptedId;

    @Column(name = "tutor_accepted")
    private Boolean tutorAccepted;

    @Column(name = "tutor_accepted_id")
    private Integer tutorAcceptedId;

    @Column(name = "modified_timestamp")
    private Instant modifiedTimestamp;

    @Column(name = "tutor_rejected_count")
    private Integer tutorRejectedCount;

    @Column(name = "cancelled")
    private Boolean cancelled;

    @Column(name = "request_times")
    private String requestTimes;

    @Column(name = "read_by_admin")
    private Boolean readByAdmin;

    @OneToMany(mappedBy = "booking")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingUserDetails> bookingUserDetails = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("bookings")
    private Subject subject;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "booking_user_info",
               joinColumns = @JoinColumn(name = "bookings_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_infos_id", referencedColumnName = "id"))
    private Set<UserInfo> userInfos = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "booking_topic",
               joinColumns = @JoinColumn(name = "bookings_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "topics_id", referencedColumnName = "id"))
    private Set<Topic> topics = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();
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

    public Booking title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public Booking requestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Booking startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Booking endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getUserComments() {
        return userComments;
    }

    public Booking userComments(String userComments) {
        this.userComments = userComments;
        return this;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public OrdinalScale getImportanceLevel() {
        return importanceLevel;
    }

    public Booking importanceLevel(OrdinalScale importanceLevel) {
        this.importanceLevel = importanceLevel;
        return this;
    }

    public void setImportanceLevel(OrdinalScale importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    public Integer getAdminAcceptedId() {
        return adminAcceptedId;
    }

    public Booking adminAcceptedId(Integer adminAcceptedId) {
        this.adminAcceptedId = adminAcceptedId;
        return this;
    }

    public void setAdminAcceptedId(Integer adminAcceptedId) {
        this.adminAcceptedId = adminAcceptedId;
    }

    public Boolean isTutorAccepted() {
        return tutorAccepted;
    }

    public Booking tutorAccepted(Boolean tutorAccepted) {
        this.tutorAccepted = tutorAccepted;
        return this;
    }

    public void setTutorAccepted(Boolean tutorAccepted) {
        this.tutorAccepted = tutorAccepted;
    }

    public Integer getTutorAcceptedId() {
        return tutorAcceptedId;
    }

    public Booking tutorAcceptedId(Integer tutorAcceptedId) {
        this.tutorAcceptedId = tutorAcceptedId;
        return this;
    }

    public void setTutorAcceptedId(Integer tutorAcceptedId) {
        this.tutorAcceptedId = tutorAcceptedId;
    }

    public Instant getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public Booking modifiedTimestamp(Instant modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
        return this;
    }

    public void setModifiedTimestamp(Instant modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public Integer getTutorRejectedCount() {
        return tutorRejectedCount;
    }

    public Booking tutorRejectedCount(Integer tutorRejectedCount) {
        this.tutorRejectedCount = tutorRejectedCount;
        return this;
    }

    public void setTutorRejectedCount(Integer tutorRejectedCount) {
        this.tutorRejectedCount = tutorRejectedCount;
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public Booking cancelled(Boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getRequestTimes() {
        return requestTimes;
    }

    public Booking requestTimes(String requestTimes) {
        this.requestTimes = requestTimes;
        return this;
    }

    public void setRequestTimes(String requestTimes) {
        this.requestTimes = requestTimes;
    }

    public Boolean isReadByAdmin() {
        return readByAdmin;
    }

    public Booking readByAdmin(Boolean readByAdmin) {
        this.readByAdmin = readByAdmin;
        return this;
    }

    public void setReadByAdmin(Boolean readByAdmin) {
        this.readByAdmin = readByAdmin;
    }

    public Set<BookingUserDetails> getBookingUserDetails() {
        return bookingUserDetails;
    }

    public Booking bookingUserDetails(Set<BookingUserDetails> bookingUserDetails) {
        this.bookingUserDetails = bookingUserDetails;
        return this;
    }

    public Booking addBookingUserDetails(BookingUserDetails bookingUserDetails) {
        this.bookingUserDetails.add(bookingUserDetails);
        bookingUserDetails.setBooking(this);
        return this;
    }

    public Booking removeBookingUserDetails(BookingUserDetails bookingUserDetails) {
        this.bookingUserDetails.remove(bookingUserDetails);
        bookingUserDetails.setBooking(null);
        return this;
    }

    public void setBookingUserDetails(Set<BookingUserDetails> bookingUserDetails) {
        this.bookingUserDetails = bookingUserDetails;
    }

    public Subject getSubject() {
        return subject;
    }

    public Booking subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<UserInfo> getUserInfos() {
        return userInfos;
    }

    public Booking userInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
        return this;
    }

    public Booking addUserInfo(UserInfo userInfo) {
        this.userInfos.add(userInfo);
        userInfo.getBookings().add(this);
        return this;
    }

    public Booking removeUserInfo(UserInfo userInfo) {
        this.userInfos.remove(userInfo);
        userInfo.getBookings().remove(this);
        return this;
    }

    public void setUserInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public Booking topics(Set<Topic> topics) {
        this.topics = topics;
        return this;
    }

    public Booking addTopic(Topic topic) {
        this.topics.add(topic);
        topic.getBookings().add(this);
        return this;
    }

    public Booking removeTopic(Topic topic) {
        this.topics.remove(topic);
        topic.getBookings().remove(this);
        return this;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Booking notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Booking addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setBooking(this);
        return this;
    }

    public Booking removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setBooking(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
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
        Booking booking = (Booking) o;
        if (booking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), booking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Booking{" +
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
            ", readByAdmin='" + isReadByAdmin() + "'" +
            "}";
    }
}
