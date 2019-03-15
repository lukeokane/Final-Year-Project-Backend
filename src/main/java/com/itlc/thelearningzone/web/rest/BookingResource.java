package com.itlc.thelearningzone.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itlc.thelearningzone.service.BookingService;
import com.itlc.thelearningzone.service.BookingUserDetailsService;
import com.itlc.thelearningzone.service.SubjectService;
import com.itlc.thelearningzone.web.rest.errors.BadRequestAlertException;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;
import com.itlc.thelearningzone.web.rest.util.PaginationUtil;
import com.itlc.thelearningzone.service.dto.BookingDTO;
import com.itlc.thelearningzone.service.dto.BookingDetailsDTO;
import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;
import com.itlc.thelearningzone.service.dto.MessageDTO;
import com.itlc.thelearningzone.service.dto.SubjectDTO;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Booking.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";
    
    private static final String ID_NULL = "idnull";
    
    private static final String DATE_SUFFIX = "T00:00:00Z";

    private final BookingService bookingService;
    
    private final SubjectService subjectService;

    private final BookingUserDetailsService bookingUserDetailsService;

    public BookingResource(BookingService bookingService, SubjectService subjectService, BookingUserDetailsService bookingUserDetailsService) {
        this.bookingService = bookingService;
        this.subjectService = subjectService;
        this.bookingUserDetailsService = bookingUserDetailsService;
    }

    /**
     * POST  /bookings : Create a new booking.
     *
     * @param bookingDTO the bookingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingDTO, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bookings")
    @Timed
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", bookingDTO);
        if (bookingDTO.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /bookings : Create a new booking.
     *
     * @param bookingDTO the bookingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingDTO, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings/edit")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingEdited(@Valid @RequestBody BookingDetailsDTO bookingDetailsDTO) throws URISyntaxException {
    	log.debug("REST request to update Booking : {}", bookingDetailsDTO);
    	System.out.println("IN" + bookingDetailsDTO.getBooking().toString());
        if (bookingDetailsDTO.getBooking().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.updateBooking(bookingDetailsDTO.getBooking(), bookingDetailsDTO.getMessage());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    
    /**
     * POST  /bookings : Create a new booking and create a booking notification for the ITLC admin for a new booking request
     *
     * @param bookingDTO the bookingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingDTO, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("bookings/createBookingWithAdminNotification")
    @Timed
    public ResponseEntity<BookingDTO> createBookingAdminNotification(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", bookingDTO);
        if (bookingDTO.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookingDTO result = bookingService.save(bookingDTO);
        bookingService.saveBookingWithAdminNotification(result);
        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /bookings/updateBookingAcceptedTutorAssigned : Create a new booking and create a booking notification for the ITLC admin for a new booking request
     *
     * @param bookingId the booking ID of the booking to modify
     * @param adminId the admin accepted ID of the booking
     * @param tutorId the tutor accepted ID of the booking
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingDTO, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("bookings/updateBookingAcceptedTutorAssigned")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingAcceptedTutorAssigned(@RequestParam Long bookingId,
    		@RequestParam Integer adminId, 
    		@RequestParam Integer tutorId) throws URISyntaxException {
        log.debug("REST request to update booking ID {}, accepted by admin ID {} and assigned to tutor ID {}", bookingId, adminId, tutorId);
        
        bookingService.updateBookingAcceptedTutorAssigned(bookingId, adminId, tutorId);
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingId.toString())).build();
    }

    /**
     * PUT  /bookings : Updates an existing booking.
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings")
    @Timed
    public ResponseEntity<BookingDTO> updateBooking(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }
    
    
    /**
     * PUT  /bookings : Updates an existing booking by admin to be assigned to a tutor and creates a notification for the tutor of a job offer
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings/updateBookingAssignTutor")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingAssignedToTutor(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.updateBookingAssignedTutor(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /bookings : Updates an existing booking to a boolean status of accepted by the tutor who the booking was assigned to and creates acceptance notification to user who requested tutorial
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings/updateBookingAcceptedByTutor")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingAcceptedByTutor(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.updateBookingAccepted(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /bookings : Updates an existing booking to a boolean status of rejected by the tutor who the booking was assigned to and creates rejection notification to the admin who assigned the tutorial
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings/updateBookingRejectedByTutor")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingRejectedByTutor(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.updateBookingRejectedByTutor(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /bookings : Updates an existing booking to a status of cancelled. Creates a cancellation notification for all users who have registered
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings/updateBookingCancelledByTutor")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingToCancelled(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.updateBookingCancelled(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /bookings : Updates a booking request to cancelled. Creates a rejection notification for the student who requested the tutorial
     *
     * @param bookingDTO the bookingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingDTO,
     * or with status 400 (Bad Request) if the bookingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bookings/updateBookingRequestRejectedByAdmin")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingRequestRejectedByAdmin(@Valid @RequestBody BookingDTO bookingDTO,
    		@RequestParam(required = false) MessageDTO message) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, ID_NULL);
        }
        BookingDTO result = bookingService.updateBookingRequestRejectedByAdmin(bookingDTO, message);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bookings : get all the bookings.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param endTime the bookings to return that begin before this time in milliseconds 
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookings")
    @Timed
    public ResponseEntity<List<BookingDTO>> getAllBookings(Pageable pageable, 
    		@RequestParam(required = false, defaultValue = "false") boolean eagerload, 
    		@RequestParam(required = false) Long startTimeMs,
    		@RequestParam(required = false) Long endTimeMs,
    		@RequestParam(required = false) Long userId, 
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of Bookings");
    	
        Page<BookingDTO> page;
        
        // If start time and end time present, get bookings between the times
        if (startTimeMs != null && endTimeMs != null) {
        	// No id passed, get all bookings
        	if (userId == null)
        	{
        	page = bookingService.findAllInTimeFrame(pageable, Instant.ofEpochMilli(startTimeMs), Instant.ofEpochMilli(endTimeMs));
        	}
        	// If id is present, get bookings for particular user.
        	else {
        	page = bookingService.findUserBookingsInTimeFrame(pageable, userId, Instant.ofEpochMilli(startTimeMs), Instant.ofEpochMilli(endTimeMs));
        	}
        // If userId is not null, get all user bookings
        } else if (userId != null) {
        	page = bookingService.findUserBookings(pageable, userId);
        } else if (eagerload) {
            page = bookingService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingService.findAll(pageable);
        }       
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookings?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsDetails : get all the bookings and include booking's subject.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param endTime the bookings to return that begin before this time in milliseconds 
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsDetails")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getAllBookingsDetails(Pageable pageable, 
    		@RequestParam(required = false, defaultValue = "false") boolean eagerload, 
    		@RequestParam(required = false) Long startTimeMs,
    		@RequestParam(required = false) Long endTimeMs,
    		@RequestParam(required = false) Long userId,
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of Bookings with their Subjects");
        Page<BookingDTO> bookings;
        Page<BookingDetailsDTO> page;
        
        // If start time and end time present, get bookings between the times
        if (startTimeMs != null && endTimeMs != null) {
        	// No id passed, get all bookings
        	if (userId == null)
        	{
        	bookings = bookingService.findAllInTimeFrame(pageable, Instant.ofEpochMilli(startTimeMs), Instant.ofEpochMilli(endTimeMs));
        	}
        	// If id is present, get bookings for particular user
        	else {
        	bookings = bookingService.findUserBookingsInTimeFrame(pageable, userId, Instant.ofEpochMilli(startTimeMs), Instant.ofEpochMilli(endTimeMs));
        	}
        // If userId is not null, get all user bookings
        } else if (userId != null) {
        	bookings = bookingService.findUserBookings(pageable, userId);
        } else if (eagerload) {
        	bookings = bookingService.findAllWithEagerRelationships(pageable);
        } else {
        	bookings = bookingService.findAll(pageable);
        }
        
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookings?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsDetailsLatestChanges : get all the bookings and include booking's subject modified after the passed in time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsLatestDetailsChanges")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsLatestDetailsChanges(Pageable pageable, 
    		@RequestParam(required = true) Long startTimeMs,
    		@RequestParam(required = false) Long userId,
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of Bookings with their Subjects after {}", startTimeMs);
        Page<BookingDTO> bookings = null;
        Page<BookingDetailsDTO> page;
        
        if (startTimeMs != null) {
        	if (userId == null) {
        		bookings = bookingService.findBookingsModifiedAfterTime(pageable, Instant.ofEpochMilli(startTimeMs));
        	} else {
        		bookings = bookingService.findUserBookingsModifiedAfterTime(pageable, userId, Instant.ofEpochMilli(startTimeMs));
        	}
        }
        else {
        	throw new BadRequestAlertException("Parameter startTimeMs is missing", ENTITY_NAME, "missing.required.parameters");
        }
             
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsDetailsChanges"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsConfirmed : get all confirmed bookings.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param endTime the bookings to return that begin before this time in milliseconds
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsConfirmed")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getConfirmedBookings(Pageable pageable, 
    		@RequestParam(required = false) Long startTimeMs,
    		@RequestParam(required = false) Long endTimeMs,
    		@RequestParam(required = false) Long userId, 
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of confirmed Bookings");
    	
        Page<BookingDTO> bookings;
        
        // If start time and end time present, get bookings between the times
        if (startTimeMs != null && endTimeMs != null) {
        	// No id passed, get all bookings
        	if (userId == null)
        	{
        	bookings = bookingService.findConfirmedInTimeFrame(pageable, Instant.ofEpochMilli(startTimeMs), Instant.ofEpochMilli(endTimeMs));
        	}
        	// If id is present, get bookings for particular user.
        	else {
        	bookings = bookingService.findConfirmedUserBookingsInTimeFrame(pageable, userId, Instant.ofEpochMilli(startTimeMs), Instant.ofEpochMilli(endTimeMs));
        	}
        // If userId is not null, get all user bookings
        } else if (userId != null) {
        	bookings = bookingService.findUserConfirmedBookings(pageable, userId);
        } else { 
        	bookings = bookingService.findConfirmedBookings(pageable);
        }       
        
        Page<BookingDetailsDTO> page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsConfirmed"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsDetailsChanges : get all the bookings and include booking's subject modified after the passed in time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsLatestConfirmedChanges")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsLatestConfirmedChanges(Pageable pageable, 
    		@RequestParam(required = true) Long startTimeMs,
    		@RequestParam(required = false) Long userId,
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of confirmed Bookings with their Subjects after {}", startTimeMs);
        Page<BookingDTO> bookings = null;
        Page<BookingDetailsDTO> page;
        
        if (startTimeMs != null) {
        	if (userId == null) {
        		bookings = bookingService.findConfirmedBookingsModifiedAfterTime(pageable, Instant.ofEpochMilli(startTimeMs));
        	} else {
        		bookings = bookingService.findUserConfirmedBookingsModifiedAfterTime(pageable, userId, Instant.ofEpochMilli(startTimeMs));
        	}
        }
        else {
        	throw new BadRequestAlertException("Parameter startTimeMs is missing", ENTITY_NAME, "missing.required.parameters");
        }
             
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsConfirmedLatestChanges"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsPendingApproval : get all the bookings pending admin approval and include booking's subject.
     *
     * @param pageable the pagination information
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsPendingApproval")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsPendingAdminApproval(Pageable pageable, 
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of Bookings pending admin approval with their Subjects");
        Page<BookingDTO> bookings = null;
        Page<BookingDetailsDTO> page;
        
        bookings = bookingService.findBookingsPendingAdminApproval(pageable);
   
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsPendingApproval"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsPendingApprovalLatestChanges : get all the bookings pending admin approval and include booking's subject modified after the passed in time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that begin after this time in milliseconds 
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsLatestPendingApprovalChanges")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsPendingAdminApprovalChanges(Pageable pageable, 
    		@RequestParam(required = true) Long startTimeMs,
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of Bookings pending admin approval with their Subjects after {}", startTimeMs);
        Page<BookingDTO> bookings = null;
        Page<BookingDetailsDTO> page;
        
        if (startTimeMs != null) {
        		bookings = bookingService.findBookingsPendingAdminApprovalModifiedAfterTime(pageable, Instant.ofEpochMilli(startTimeMs));
        }
        else {
        	throw new BadRequestAlertException("Parameter startTimeMs is missing", ENTITY_NAME, "missing.required.parameters");
        }
             
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsPendingApprovalLatestChanges"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsTutors : get bookings for a tutor including booking's subject modified after the passed in time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that are modified after this time in milliseconds 
     * @param userId the ID of the tutor/user to retrieve bookings pending their acceptance
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsTutors")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getTutorBookings(Pageable pageable,
    		@RequestParam(required = false, defaultValue = "false") boolean pending,
    		@RequestParam(required = true) Long userId,
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of tutor pending Bookings with their Subjects associated with user {}", userId);
        Page<BookingDTO> bookings = null;
        Page<BookingDetailsDTO> page;
        
        if (userId == null)
        {
        	throw new BadRequestAlertException("Parameter userId is missing", ENTITY_NAME, "missing.required.parameters");
        }
        
        if (pending == false)
        {
        	// get all confirmed tutor bookings
        	bookings = bookingService.findTutorBookings(pageable, userId);
        }
        else {
        	// get all pending tutor bookings
        	bookings = bookingService.findTutorPendingRequestsBookings(pageable, userId);
        }
        
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
        
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsTutors"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /bookingsTutorLatestChanges : get bookings for tutor (pending or not pending) and include booking's subject modified after the passed in time.
     *
     * @param pageable the pagination information
     * @param startTime the bookings to return that are modified after this time in milliseconds
     * @param pending the flag to determine whether to get booking pending or confirmed for a tutor  
     * @param userId the ID of the tutor/user to retrieve bookings pending their acceptance
     * @param userInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     */
    @GetMapping("/bookingsLatestTutorChanges")
    @Timed
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsLatestTutorChanges(Pageable pageable,
    		@RequestParam(required = true) Long startTimeMs,
    		@RequestParam(required = false, defaultValue = "false") boolean pending,
    		@RequestParam(required = true) Long userId,
    		@RequestParam(required = false, defaultValue = "false") boolean userInfo) {
    	
    	log.debug("REST request to get a page of tutor pending Bookings with their Subjects modified after {} associated with user {}", startTimeMs, userId);
        Page<BookingDTO> bookings = null;
        Page<BookingDetailsDTO> page;
        
        if (startTimeMs == null || userId == null)
        {
        	throw new BadRequestAlertException("Parameter startTimeMs or userId is missing", ENTITY_NAME, "missing.required.parameters");
        }
        
        if (pending == false)
        {
        	// get all confirmed tutor bookings after the time passed in
        	bookings = bookingService.findTutorBookingsModifiedAfterTime(pageable, userId, Instant.ofEpochMilli(startTimeMs));
        }
        else {
        	// get all pending tutor bookings after the time passed in
        	bookings = bookingService.findTutorPendingRequestsBookingsModifiedAfterTime(pageable, userId, Instant.ofEpochMilli(startTimeMs));
        }
        
        page = BookingsDetailsDTOHandler(bookings.getContent(), userInfo);
        
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookingsTutorLatestChanges"));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    public Page<BookingDetailsDTO> BookingsDetailsDTOHandler(List<BookingDTO> bookingsDTOList, boolean userInfo) {
    	
    	
    	// Create ArrayList for BookingDetails
    	List<BookingDetailsDTO> bookingDetailsList = new ArrayList<BookingDetailsDTO>();
    	
    	// Get size of list
    	long pageListSize = bookingsDTOList.size();
    	log.debug("getAllBookingDetails - returned {} results", pageListSize);
		log.debug("getAllBookingDetails - userInfo set to {}", userInfo);
    	// Iterate through bookings, get booking subject, add both to BookingDetailsDTO
    	for (int i = 0; i < pageListSize; i++) {
    			BookingDetailsDTO bdDTO = new BookingDetailsDTO();
    			
    			// Get booking and subject
    			bdDTO.setBooking(bookingsDTOList.get(i));
    			
    			// Get subject if booking containers subject ID
    			if (bookingsDTOList.get(i).getSubjectId() != null) {
    				
    				Long subjectId = bookingsDTOList.get(i).getSubjectId();
    				SubjectDTO subject = subjectService.findOne(subjectId).orElseThrow(() -> new BadRequestAlertException("Subject id " + subjectId + " is does not exist", ENTITY_NAME, ID_NULL));
    				bdDTO.setSubject(subject);
    			}
    			
    			// Do not return any list of UserInfo objects or BookingUserDetail objects 
    			if (!userInfo)
    			{
    			bdDTO.getBooking().setUserInfos(null);
    			bdDTO.getBooking().setBookingUserDetailsDTO(null);
    			}
    			
    			bookingDetailsList.add(bdDTO);
    		}
    	
    	return new PageImpl<BookingDetailsDTO>(bookingDetailsList);
    }
    
    
    /**
     * GET  /bookings/ get all the bookings in a list form between a start date and end date booking user details are populated
     *
     * @param id the id of the bookingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bookings/findAllBookingsList/{fromDate}/toDate/{toDate}")
    @Timed
    public ResponseEntity<List<BookingDTO>> findAllBookingsListWithUserDetails(@PathVariable String fromDate,
			@PathVariable String toDate) {
        log.debug("REST request to get Booking list form");

        fromDate= fromDate + DATE_SUFFIX;
        toDate = toDate + DATE_SUFFIX;
        Instant instantFromDate = Instant.parse(fromDate);
        Instant instantToDate = Instant.parse(toDate);
        
        List<BookingDTO> bookings = bookingService.findAllBookingsList(instantFromDate,instantToDate);
        for (BookingDTO booking : bookings)
        {
        	Set<BookingUserDetailsDTO> bookingUserDetailsDTO2 = new HashSet<>(); 	
        	bookingUserDetailsDTO2 = bookingUserDetailsService.findAllByBookingId(booking.getId());
        	booking.setBookingUserDetailsDTO(bookingUserDetailsDTO2);
        }
        
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bookings));
    }
    
    
    
    /**
     * GET  /bookings/ get all the bookings in a list form with all courses and a selected year between a start date and end date with booking user details populated
     *
     * @param id the id of the bookingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bookings/findAllBookingsAllCoursesSelectedYear/{fromDate}/toDate/{toDate}/selectedYear/{selectedYear}")
    @Timed
    public ResponseEntity<List<BookingDTO>> findAllBookingsAllCoursesSelectedYear(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable Integer selectedYear) {
        log.debug("REST request to get Booking list form");

        fromDate= fromDate + DATE_SUFFIX;
        toDate = toDate + DATE_SUFFIX;
        Instant instantFromDate = Instant.parse(fromDate);
        Instant instantToDate = Instant.parse(toDate);
        List<BookingDTO> bookings = bookingService.findAllBookingsAllCoursesSelectedYearBetweenDates(instantFromDate, instantToDate, selectedYear);        
        for (BookingDTO booking : bookings)
        {
        	Set<BookingUserDetailsDTO> bookingUserDetailsDTO2 = new HashSet<>(); 	
        	bookingUserDetailsDTO2 = bookingUserDetailsService.findAllByBookingId(booking.getId());
        	booking.setBookingUserDetailsDTO(bookingUserDetailsDTO2);
        
        }      
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bookings));
    }

    
    /**
     * GET  /bookings/ get all the bookings in a list form with a selected course and a selected year between a start date and end date with booking user details populated
     *
     * @param id the id of the bookingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bookings/findAllBookingsSelectedCourseAndSelectedYear/{fromDate}/toDate/{toDate}/selectedCourse/{courseId}/selectedYear/{selectedYear}")
    @Timed
    public ResponseEntity<List<BookingDTO>> findAllBookingsAllCoursesSelectedYearBetweenDates(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable Integer courseId, @PathVariable Integer selectedYear) {
        log.debug("REST request to get Booking list form");

        fromDate= fromDate + DATE_SUFFIX;
        toDate = toDate + DATE_SUFFIX;
        Instant instantFromDate = Instant.parse(fromDate);
        Instant instantToDate = Instant.parse(toDate);
        List<BookingDTO> bookings = bookingService.findAllBookingsSelectedCourseSelectedYearBetweenDates(instantFromDate, instantToDate, courseId, selectedYear);        
        for (BookingDTO booking : bookings)
        {
        	Set<BookingUserDetailsDTO> bookingUserDetailsDTO2 = new HashSet<>(); 	
        	bookingUserDetailsDTO2 = bookingUserDetailsService.findAllByBookingId(booking.getId());
        	booking.setBookingUserDetailsDTO(bookingUserDetailsDTO2);
        
        }      
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bookings));
    }
    
    /**
     * GET  /bookings/ get all the bookings in a list form with a selected course and all years between a start date and end date with booking user details populated
     *
     * @param id the id of the bookingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bookings/findAllBookingsSelectedCourseAndAllYears/{fromDate}/toDate/{toDate}/selectedCourse/{courseId}")
    @Timed
    public ResponseEntity<List<BookingDTO>> findAllBookingsSelectedCourseAllYearsBetweenDates(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable Integer courseId) {
        log.debug("REST request to get Booking list form");

        fromDate= fromDate + DATE_SUFFIX;
        toDate = toDate + DATE_SUFFIX;
        Instant instantFromDate = Instant.parse(fromDate);
        Instant instantToDate = Instant.parse(toDate);
        List<BookingDTO> bookings = bookingService.findAllBookingsSelectedCourseAllYearsBetweenDates(instantFromDate, instantToDate, courseId);        
        for (BookingDTO booking : bookings)
        {
        	Set<BookingUserDetailsDTO> bookingUserDetailsDTO2 = new HashSet<>(); 	
        	bookingUserDetailsDTO2 = bookingUserDetailsService.findAllByBookingId(booking.getId());
        	booking.setBookingUserDetailsDTO(bookingUserDetailsDTO2);
        
        }      
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bookings));
    }
    /**
     * GET  /bookings/:id : get the "id" booking.
     *
     * @param id the id of the bookingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bookings/{id}")
    @Timed
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<BookingDTO> bookingDTO = bookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingDTO);
    }

    /**
     * DELETE  /bookings/:id : delete the "id" booking.
     *
     * @param id the id of the bookingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bookings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @PutMapping("/bookings/cancelBooking/{bookingID}")
    @Timed
    public ResponseEntity<BookingDTO> updateBookingForCancellation(@PathVariable Long bookingID) throws URISyntaxException {
    	log.debug("REST request to update Booking : {}", bookingID);
        BookingDTO result = bookingService.cancelBooking(bookingID);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
