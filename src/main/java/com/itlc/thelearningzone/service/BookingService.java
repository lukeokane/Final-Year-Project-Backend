package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.service.dto.BookingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
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
     * Get all the bookings within a time frame.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingDTO> findAllInTimeFrame(Pageable pageable, Instant startTime, Instant endTime);
    
    /**
     * Get all the bookings within a time frame associated with a user ID.
     *
     * @param pageable the pagination information
     * @param userId ID of the user
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param endTime the bookings to return that begin before this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findUserBookingsInTimeFrame(Pageable pageable, Long userId, Instant startTime, Instant endTime);

    /**
     * Get all the bookings associated with a user ID.
     * @param pageable the pagination information
     * @param userId ID of the user
     */
    Page<BookingDTO> findUserBookings(Pageable pageable, @Param("userId") Long userId);
    
    /**
     * Get all the bookings modified after a time associated with a user ID.
     *
     * @param pageable the pagination information
     * @param userId ID of the user
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findUserBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime);
    
    /**
     * Get all the bookings modified after a time.
     *
     * @param pageable the pagination information
     * @param userId ID of the user
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findBookingsModifiedAfterTime(Pageable pageable, Instant startTime);
    
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
	
	BookingDTO updateBookingRequestRejectedByAdmin(@Valid BookingDTO bookingDTO);
}
