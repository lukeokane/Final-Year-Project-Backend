package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;

/**
 * A BookingUserDetails.
 */
@Entity
@Table(name = "booking_user_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BookingUserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_feedback")
    private String userFeedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_satisfaction")
    private OrdinalScale userSatisfaction;

    @Column(name = "usercheck_in_time")
    private Instant usercheckInTime;

    @Column(name = "usercheck_out_time")
    private Instant usercheckOutTime;

    @Column(name = "user_cancelled")
    private Boolean userCancelled;

    @Column(name = "tutor_rejected")
    private Boolean tutorRejected;

    @ManyToOne
    @JsonIgnoreProperties("bookingUserDetails")
    private UserInfo userInfo;

    @ManyToOne
    @JsonIgnoreProperties("bookingUserDetails")
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public BookingUserDetails userFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
        return this;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public OrdinalScale getUserSatisfaction() {
        return userSatisfaction;
    }

    public BookingUserDetails userSatisfaction(OrdinalScale userSatisfaction) {
        this.userSatisfaction = userSatisfaction;
        return this;
    }

    public void setUserSatisfaction(OrdinalScale userSatisfaction) {
        this.userSatisfaction = userSatisfaction;
    }

    public Instant getUsercheckInTime() {
        return usercheckInTime;
    }

    public BookingUserDetails usercheckInTime(Instant usercheckInTime) {
        this.usercheckInTime = usercheckInTime;
        return this;
    }

    public void setUsercheckInTime(Instant usercheckInTime) {
        this.usercheckInTime = usercheckInTime;
    }

    public Instant getUsercheckOutTime() {
        return usercheckOutTime;
    }

    public BookingUserDetails usercheckOutTime(Instant usercheckOutTime) {
        this.usercheckOutTime = usercheckOutTime;
        return this;
    }

    public void setUsercheckOutTime(Instant usercheckOutTime) {
        this.usercheckOutTime = usercheckOutTime;
    }

    public Boolean isUserCancelled() {
        return userCancelled;
    }

    public BookingUserDetails userCancelled(Boolean userCancelled) {
        this.userCancelled = userCancelled;
        return this;
    }

    public void setUserCancelled(Boolean userCancelled) {
        this.userCancelled = userCancelled;
    }

    public Boolean isTutorRejected() {
        return tutorRejected;
    }

    public BookingUserDetails tutorRejected(Boolean tutorRejected) {
        this.tutorRejected = tutorRejected;
        return this;
    }

    public void setTutorRejected(Boolean tutorRejected) {
        this.tutorRejected = tutorRejected;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public BookingUserDetails userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Booking getBooking() {
        return booking;
    }

    public BookingUserDetails booking(Booking booking) {
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
        BookingUserDetails bookingUserDetails = (BookingUserDetails) o;
        if (bookingUserDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookingUserDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BookingUserDetails{" +
            "id=" + getId() +
            ", userFeedback='" + getUserFeedback() + "'" +
            ", userSatisfaction='" + getUserSatisfaction() + "'" +
            ", usercheckInTime='" + getUsercheckInTime() + "'" +
            ", usercheckOutTime='" + getUsercheckOutTime() + "'" +
            ", userCancelled='" + isUserCancelled() + "'" +
            ", tutorRejected='" + isTutorRejected() + "'" +
            "}";
    }
}
