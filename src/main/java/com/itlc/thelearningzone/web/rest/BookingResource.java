package com.itlc.thelearningzone.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itlc.thelearningzone.service.BookingService;
import com.itlc.thelearningzone.service.SubjectService;
import com.itlc.thelearningzone.web.rest.errors.BadRequestAlertException;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;
import com.itlc.thelearningzone.web.rest.util.PaginationUtil;
import com.itlc.thelearningzone.service.dto.BookingDTO;
import com.itlc.thelearningzone.service.dto.BookingDetailsDTO;
import com.itlc.thelearningzone.service.dto.SubjectDTO;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Booking.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";

    private final BookingService bookingService;
    
    private final SubjectService subjectService;

    public BookingResource(BookingService bookingService, SubjectService subjectService) {
        this.bookingService = bookingService;
        this.subjectService = subjectService;
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
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookingDTO result = bookingService.updateBookingRejected(bookingDTO);
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
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
    public ResponseEntity<BookingDTO> updateBookingRequestRejectedByAdmin(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookingDTO result = bookingService.updateBookingRequestRejectedByAdmin(bookingDTO);
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
     * @param allUserInfo flag to decide whether to return all user information (UserInfos & BookingUserDetails)
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
        
        // Convert Page to List
    	List<BookingDTO> pageList = bookings.getContent();
    	
    	// Create ArrayList for BookingDetails
    	List<BookingDetailsDTO> bookingDetailsList = new ArrayList<BookingDetailsDTO>();
    	
    	// Get size of list
    	long pageListSize = pageList.size();
    	log.debug("getAllBookingDetails - returned {} results", pageListSize);
		log.debug("getAllBookingDetails - userInfo set to {}", userInfo);
    	// Iterate through bookings, get booking subject, add both to BookingDetailsDTO
    	for (int i = 0; i < pageListSize; i++) {
    			BookingDetailsDTO bdDTO = new BookingDetailsDTO();
    			
    			// Get booking and subject
    			bdDTO.booking = pageList.get(i);
    			
    			// Get subject if booking containers subject ID
    			if (pageList.get(i).getSubjectId() != null) {
    				bdDTO.subject = subjectService.findOne(pageList.get(i).getId()).get();
    			}
    			
    			// Do not return any list of UserInfo objects or BookingUserDetail objects 
    			if (!userInfo)
    			{
    			bdDTO.booking.setUserInfos(null);
    			bdDTO.booking.setBookingUserDetailsDTO(null);
    			}
    			
    			bookingDetailsList.add(bdDTO);
    		}
    	
    	page = new PageImpl<BookingDetailsDTO>(bookingDetailsList);
    	
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bookings?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
}
