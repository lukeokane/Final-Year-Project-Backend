package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "tutor_skills")
    private String tutorSkills;

    @Column(name = "profile_image_url")
    private String profileImageURL;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("userInfos")
    private SemesterGroup semesterGroup;

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingUserDetails> bookingUserDetails = new HashSet<>();
    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> sentNotifications = new HashSet<>();
    @OneToMany(mappedBy = "receiver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> receivedNotifications = new HashSet<>();
    @ManyToMany(mappedBy = "userInfos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTutorSkills() {
        return tutorSkills;
    }

    public UserInfo tutorSkills(String tutorSkills) {
        this.tutorSkills = tutorSkills;
        return this;
    }

    public void setTutorSkills(String tutorSkills) {
        this.tutorSkills = tutorSkills;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public UserInfo profileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
        return this;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public User getUser() {
        return user;
    }

    public UserInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SemesterGroup getSemesterGroup() {
        return semesterGroup;
    }

    public UserInfo semesterGroup(SemesterGroup semesterGroup) {
        this.semesterGroup = semesterGroup;
        return this;
    }

    public void setSemesterGroup(SemesterGroup semesterGroup) {
        this.semesterGroup = semesterGroup;
    }

    public Set<BookingUserDetails> getBookingUserDetails() {
        return bookingUserDetails;
    }

    public UserInfo bookingUserDetails(Set<BookingUserDetails> bookingUserDetails) {
        this.bookingUserDetails = bookingUserDetails;
        return this;
    }

    public UserInfo addBookingUserDetails(BookingUserDetails bookingUserDetails) {
        this.bookingUserDetails.add(bookingUserDetails);
        bookingUserDetails.setUserInfo(this);
        return this;
    }

    public UserInfo removeBookingUserDetails(BookingUserDetails bookingUserDetails) {
        this.bookingUserDetails.remove(bookingUserDetails);
        bookingUserDetails.setUserInfo(null);
        return this;
    }

    public void setBookingUserDetails(Set<BookingUserDetails> bookingUserDetails) {
        this.bookingUserDetails = bookingUserDetails;
    }

    public Set<Notification> getSentNotifications() {
        return sentNotifications;
    }

    public UserInfo sentNotifications(Set<Notification> notifications) {
        this.sentNotifications = notifications;
        return this;
    }

    public UserInfo addSentNotifications(Notification notification) {
        this.sentNotifications.add(notification);
        notification.setSender(this);
        return this;
    }

    public UserInfo removeSentNotifications(Notification notification) {
        this.sentNotifications.remove(notification);
        notification.setSender(null);
        return this;
    }

    public void setSentNotifications(Set<Notification> notifications) {
        this.sentNotifications = notifications;
    }

    public Set<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public UserInfo receivedNotifications(Set<Notification> notifications) {
        this.receivedNotifications = notifications;
        return this;
    }

    public UserInfo addReceivedNotifications(Notification notification) {
        this.receivedNotifications.add(notification);
        notification.setReceiver(this);
        return this;
    }

    public UserInfo removeReceivedNotifications(Notification notification) {
        this.receivedNotifications.remove(notification);
        notification.setReceiver(null);
        return this;
    }

    public void setReceivedNotifications(Set<Notification> notifications) {
        this.receivedNotifications = notifications;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public UserInfo bookings(Set<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    public UserInfo addBooking(Booking booking) {
        this.bookings.add(booking);
        booking.getUserInfos().add(this);
        return this;
    }

    public UserInfo removeBooking(Booking booking) {
        this.bookings.remove(booking);
        booking.getUserInfos().remove(this);
        return this;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
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
        UserInfo userInfo = (UserInfo) o;
        if (userInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", tutorSkills='" + getTutorSkills() + "'" +
            ", profileImageURL='" + getProfileImageURL() + "'" +
            "}";
    }
}
