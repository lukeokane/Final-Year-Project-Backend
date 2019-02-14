package com.itlc.thelearningzone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Topic.
 */
@Entity
@Table(name = "topic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "topic")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> resources = new HashSet<>();
    @ManyToMany(mappedBy = "topics")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Subject> subjects = new HashSet<>();

    @ManyToMany(mappedBy = "topics")
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

    public String getTitle() {
        return title;
    }

    public Topic title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Topic resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Topic addResource(Resource resource) {
        this.resources.add(resource);
        resource.setTopic(this);
        return this;
    }

    public Topic removeResource(Resource resource) {
        this.resources.remove(resource);
        resource.setTopic(null);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public Topic subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public Topic addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getTopics().add(this);
        return this;
    }

    public Topic removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getTopics().remove(this);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public Topic bookings(Set<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    public Topic addBooking(Booking booking) {
        this.bookings.add(booking);
        booking.getTopics().add(this);
        return this;
    }

    public Topic removeBooking(Booking booking) {
        this.bookings.remove(booking);
        booking.getTopics().remove(this);
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
        Topic topic = (Topic) o;
        if (topic.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), topic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Topic{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
