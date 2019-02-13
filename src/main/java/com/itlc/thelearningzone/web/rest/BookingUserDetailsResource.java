package com.itlc.thelearningzone.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itlc.thelearningzone.service.BookingUserDetailsService;
import com.itlc.thelearningzone.web.rest.errors.BadRequestAlertException;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;
import com.itlc.thelearningzone.web.rest.util.PaginationUtil;
import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BookingUserDetails.
 */
@RestController
@RequestMapping("/api")
public class BookingUserDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BookingUserDetailsResource.class);

    private static final String ENTITY_NAME = "bookingUserDetails";

    private final BookingUserDetailsService bookingUserDetailsService;

    public BookingUserDetailsResource(BookingUserDetailsService bookingUserDetailsService) {
        this.bookingUserDetailsService = bookingUserDetailsService;
    }

    /**
     * POST  /booking-user-details : Create a new bookingUserDetails.
     *
     * @param bookingUserDetailsDTO the bookingUserDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingUserDetailsDTO, or with status 400 (Bad Request) if the bookingUserDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/booking-user-details")
    @Timed
    public ResponseEntity<BookingUserDetailsDTO> createBookingUserDetails(@RequestBody BookingUserDetailsDTO bookingUserDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save BookingUserDetails : {}", bookingUserDetailsDTO);
        if (bookingUserDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingUserDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookingUserDetailsDTO result = bookingUserDetailsService.save(bookingUserDetailsDTO);
        return ResponseEntity.created(new URI("/api/booking-user-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /booking-user-details : Updates an existing bookingUserDetails.
     *
     * @param bookingUserDetailsDTO the bookingUserDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingUserDetailsDTO,
     * or with status 400 (Bad Request) if the bookingUserDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookingUserDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/booking-user-details")
    @Timed
    public ResponseEntity<BookingUserDetailsDTO> updateBookingUserDetails(@RequestBody BookingUserDetailsDTO bookingUserDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update BookingUserDetails : {}", bookingUserDetailsDTO);
        if (bookingUserDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookingUserDetailsDTO result = bookingUserDetailsService.save(bookingUserDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingUserDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /booking-user-details : get all the bookingUserDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookingUserDetails in body
     */
    @GetMapping("/booking-user-details")
    @Timed
    public ResponseEntity<List<BookingUserDetailsDTO>> getAllBookingUserDetails(Pageable pageable) {
        log.debug("REST request to get a page of BookingUserDetails");
        Page<BookingUserDetailsDTO> page = bookingUserDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/booking-user-details");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /booking-user-details/:id : get the "id" bookingUserDetails.
     *
     * @param id the id of the bookingUserDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingUserDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/booking-user-details/{id}")
    @Timed
    public ResponseEntity<BookingUserDetailsDTO> getBookingUserDetails(@PathVariable Long id) {
        log.debug("REST request to get BookingUserDetails : {}", id);
        Optional<BookingUserDetailsDTO> bookingUserDetailsDTO = bookingUserDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingUserDetailsDTO);
    }

    /**
     * DELETE  /booking-user-details/:id : delete the "id" bookingUserDetails.
     *
     * @param id the id of the bookingUserDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/booking-user-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookingUserDetails(@PathVariable Long id) {
        log.debug("REST request to delete BookingUserDetails : {}", id);
        bookingUserDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @PutMapping("/booking-user-details/cancelAttendanceWithCard/{bookingID}/{studentNumber}")
    @Timed
    public ResponseEntity<BookingUserDetailsDTO> updateBookingUserDetailsForCancelledAttendance(@PathVariable Long bookingID, @PathVariable String studentNumber) throws URISyntaxException {
    	log.debug("REST request to update BookingUserDetails for student : {}", studentNumber);
        if (bookingID == null) {
            throw new BadRequestAlertException("Invalid id", "Booking", "idnull");
        }
        BookingUserDetailsDTO result = bookingUserDetailsService.cancelAttendanceWithCard(bookingID, studentNumber);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
