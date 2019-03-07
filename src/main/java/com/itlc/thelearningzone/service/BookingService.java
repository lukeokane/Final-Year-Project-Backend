package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.BookingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
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
    Page<BookingDTO> findUserBookings(Pageable pageable, Long userId);
    
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
     * Get all the pending bookings of a tutor that are modified after a time.
     *
     * @param pageable the pagination information
     * @param userId ID of the tutor 
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findTutorPendingRequestsBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime);
    
    /**
     * Get all tutor bookings modified after a time.
     *
     * @param pageable the pagination information
     * @param userId ID of the tutor 
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findTutorBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime);
    
    /**
     * Get all bookings for a tutor
     *
     * @param pageable the pagination information.
     * @param userId ID of the tutor 
     * @return the list of entities
     */
    Page<BookingDTO> findTutorBookings(Pageable pageable, Long userId);
    
    /**
     * Get all pending bookings for a tutor
     *
     * @param pageable the pagination information
     * @param userId ID of the tutor 
     * @return the list of entities
     */
    Page<BookingDTO> findTutorPendingRequestsBookings(Pageable pageable, Long userId);
    
    /**
     * Get all confirmed bookings within a time frame.
     *
     * @param pageable the pagination information
     * @param userId ID of the user
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param endTime the bookings to return that begin before this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findConfirmedInTimeFrame(Pageable pageable, Instant startTime, Instant endTime);
    
    /**
     * Get all confirmed bookings within a time frame associated with a user ID.
     *
     * @param pageable the pagination information
     * @param userId ID of the user
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param endTime the bookings to return that begin before this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findConfirmedUserBookingsInTimeFrame(Pageable pageable, Long userId, Instant startTime, Instant endTime);
    
    /**
     * Get all confirmed bookings associated with a user ID.
     *
     * @param pageable the pagination information
     * @param userId ID of the user
     * @return the list of entities
     */
    Page<BookingDTO> findUserConfirmedBookings(Pageable pageable, Long userId);
    
    /**
     * Get all confirmed bookings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingDTO> findConfirmedBookings(Pageable pageable);
    
    /**
     * Get all confirmed bookings modified after a time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findConfirmedBookingsModifiedAfterTime(Pageable pageable, Instant startTime);
    
    /**
     * Get all confirmed bookings modified after a time associated with a user ID.
     *
     * @param pageable the pagination information
     * @param userId ID of the tutor 
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findUserConfirmedBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime);

    /**
     * Get all bookings not accepted by admin modified after a time.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingDTO> findBookingsPendingAdminApproval(Pageable pageable);
    
    /**
     * Get all bookings not accepted by an admin modified after a time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that have been modified after this time in milliseconds 
     * @return the list of entities
     */
    Page<BookingDTO> findBookingsPendingAdminApprovalModifiedAfterTime(Pageable pageable, Instant startTime);
    
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
	
	void updateBookingAcceptedTutorAssigned(Long bookingId, Integer adminId, Integer tutorId);

	BookingDTO updateBookingAssignedTutor(@Valid BookingDTO bookingDTO);

	BookingDTO updateBookingRejectedByTutor(@Valid BookingDTO bookingDTO);
	
	BookingDTO updateBookingRequestRejectedByAdmin(@Valid BookingDTO bookingDTO);

	List<BookingDTO> findAllBookingsList(Instant instantFromDate, Instant instantToDate);

	//List<BookingDTO> findAllBookingsDistributionList(Instant instantFromDate, Instant instantToDate);
		
	List<BookingDTO> findAllBookingsAllCoursesSelectedYearBetweenDates(Instant instantFromDate, Instant instantToDate,
			Integer selectedYear);
	
	/**
     * Cancel the "bookingID" booking.
     *
     * @param bookingID the bookingID belonging to the booking entity to be deleted
     * @return the entity
     */
	BookingDTO cancelBooking(Long bookingID);

	List<BookingDTO> findAllBookingsSelectedCourseSelectedYearBetweenDates(Instant instantFromDate,
			Instant instantToDate, Integer courseId, Integer selectedYear);

	List<BookingDTO> findAllBookingsSelectedCourseAllYearsBetweenDates(Instant instantFromDate,
			Instant instantToDate, Integer courseId);


}
