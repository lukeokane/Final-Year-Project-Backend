package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingUserDetails> bookingUserDetails = new HashSet<>();
    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> senderUserInfos = new HashSet<>();
    @OneToMany(mappedBy = "receiver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> receiverUserInfos = new HashSet<>();
    @ManyToMany(mappedBy = "userInfos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();

    @ManyToMany(mappedBy = "userInfos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Semester> semesters = new HashSet<>();

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

    public Set<Notification> getSenderUserInfos() {
        return senderUserInfos;
    }

    public UserInfo senderUserInfos(Set<Notification> notifications) {
        this.senderUserInfos = notifications;
        return this;
    }

    public UserInfo addSenderUserInfo(Notification notification) {
        this.senderUserInfos.add(notification);
        notification.setSender(this);
        return this;
    }

    public UserInfo removeSenderUserInfo(Notification notification) {
        this.senderUserInfos.remove(notification);
        notification.setSender(null);
        return this;
    }

    public void setSenderUserInfos(Set<Notification> notifications) {
        this.senderUserInfos = notifications;
    }

    public Set<Notification> getReceiverUserInfos() {
        return receiverUserInfos;
    }

    public UserInfo receiverUserInfos(Set<Notification> notifications) {
        this.receiverUserInfos = notifications;
        return this;
    }

    public UserInfo addReceiverUserInfo(Notification notification) {
        this.receiverUserInfos.add(notification);
        notification.setReceiver(this);
        return this;
    }

    public UserInfo removeReceiverUserInfo(Notification notification) {
        this.receiverUserInfos.remove(notification);
        notification.setReceiver(null);
        return this;
    }

    public void setReceiverUserInfos(Set<Notification> notifications) {
        this.receiverUserInfos = notifications;
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

    public Set<Semester> getSemesters() {
        return semesters;
    }

    public UserInfo semesters(Set<Semester> semesters) {
        this.semesters = semesters;
        return this;
    }

    public UserInfo addSemester(Semester semester) {
        this.semesters.add(semester);
        semester.getUserInfos().add(this);
        return this;
    }

    public UserInfo removeSemester(Semester semester) {
        this.semesters.remove(semester);
        semester.getUserInfos().remove(this);
        return this;
    }

    public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
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
            "}";
    }
}
