package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing BookingUserDetails.
 */
public interface BookingUserDetailsService {

    /**
     * Save a bookingUserDetails.
     *
     * @param bookingUserDetailsDTO the entity to save
     * @return the persisted entity
     */
    BookingUserDetailsDTO save(BookingUserDetailsDTO bookingUserDetailsDTO);

    /**
     * Get all the bookingUserDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingUserDetailsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bookingUserDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BookingUserDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" bookingUserDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	Set<BookingUserDetailsDTO> findAllByBookingId(Long id);

	/**
     * Cancel tutorial attendance.
     *
     * @param bookingID the booking belonging to the booking entity
     * @param login the login belonging to the user canceling attendance
     * @return the persisted entity
     */
	BookingUserDetailsDTO cancelAttendance(Long bookingID, String login);
	
	/**
     * Check-in to a tutorial.
     *
     * @param bookingID the booking belonging to the booking entity
     * @param login the login belonging to the user checking in
     * @return the persisted entity
     */
	BookingUserDetailsDTO checkIn(Long bookingID, String login);
}
