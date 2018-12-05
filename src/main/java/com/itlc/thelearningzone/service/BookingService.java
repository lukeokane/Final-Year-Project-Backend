package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.BookingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import javax.validation.Valid;

/**
 * Service Interface for managing Booking.
 */
public interface BookingService {

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save
     * @return the persisted entity
     */
    BookingDTO save(BookingDTO bookingDTO);

    /**
     * Get all the bookings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingDTO> findAll(Pageable pageable);

    /**
     * Get all the Booking with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<BookingDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" booking.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BookingDTO> findOne(Long id);

    /**
     * Delete the "id" booking.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	BookingDTO updateBookingCancelled(@Valid BookingDTO bookingDTO);

	BookingDTO updateBookingAccepted(@Valid BookingDTO bookingDTO);

	void saveBookingWithAdminNotification(@Valid BookingDTO bookingDTO);

	BookingDTO updateBookingAssignedTutor(@Valid BookingDTO bookingDTO);

	BookingDTO updateBookingRejected(@Valid BookingDTO bookingDTO);
}
