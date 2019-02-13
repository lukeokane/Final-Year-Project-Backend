package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.BookingUserDetailsService;
import com.itlc.thelearningzone.domain.BookingUserDetails;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.repository.BookingUserDetailsRepository;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;
import com.itlc.thelearningzone.service.mapper.BookingUserDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing BookingUserDetails.
 */
@Service
@Transactional
public class BookingUserDetailsServiceImpl implements BookingUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(BookingUserDetailsServiceImpl.class);

    private final BookingUserDetailsRepository bookingUserDetailsRepository;

    private final BookingUserDetailsMapper bookingUserDetailsMapper;

    private final UserRepository userRepository;

    public BookingUserDetailsServiceImpl(BookingUserDetailsRepository bookingUserDetailsRepository, BookingUserDetailsMapper bookingUserDetailsMapper,
    		UserRepository userRepository) {
        this.bookingUserDetailsRepository = bookingUserDetailsRepository;
        this.bookingUserDetailsMapper = bookingUserDetailsMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a bookingUserDetails.
     *
     * @param bookingUserDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BookingUserDetailsDTO save(BookingUserDetailsDTO bookingUserDetailsDTO) {
        log.debug("Request to save BookingUserDetails : {}", bookingUserDetailsDTO);

        BookingUserDetails bookingUserDetails = bookingUserDetailsMapper.toEntity(bookingUserDetailsDTO);
        bookingUserDetails = bookingUserDetailsRepository.save(bookingUserDetails);
        return bookingUserDetailsMapper.toDto(bookingUserDetails);
    }

    /**
     * Get all the bookingUserDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookingUserDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BookingUserDetails");
        return bookingUserDetailsRepository.findAll(pageable)
            .map(bookingUserDetailsMapper::toDto);
    }


    /**
     * Get one bookingUserDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BookingUserDetailsDTO> findOne(Long id) {
        log.debug("Request to get BookingUserDetails : {}", id);
        return bookingUserDetailsRepository.findById(id)
            .map(bookingUserDetailsMapper::toDto);
    }

    /**
     * Delete the bookingUserDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookingUserDetails : {}", id);
        bookingUserDetailsRepository.deleteById(id);
    }

	@Override
	public Set<BookingUserDetailsDTO> findAllByBookingId(Long id) {
		Set<BookingUserDetailsDTO> list = new HashSet<>();
		Set<BookingUserDetails> ps = bookingUserDetailsRepository.findAlltest(id);
		for (BookingUserDetails p : ps)
			list.add(bookingUserDetailsMapper.toDto(p));
		return list;
	}
	
	/**
     * Cancel tutorial attendance with student card.
     *
     * @param bookingID the booking belonging to the bookingUserDetails entity
     * @param studentNumber the student number retrieved from the student card scanner
     * @return the persisted entity
     */
	@Override
    public BookingUserDetailsDTO cancelAttendanceWithCard(Long bookingID, String studentNumber) {
        log.debug("Request to cancel attendance for Student : {}", studentNumber);
        
        Optional<User> user = userRepository.findOneByLogin(studentNumber);
        if (user.isPresent()) {
        	BookingUserDetails bookingUserDetails = bookingUserDetailsRepository.findOneByBookingIdAndStudentNumber(bookingID, user.get().getId());
            bookingUserDetails.setUserCancelled(true);
            bookingUserDetails = bookingUserDetailsRepository.save(bookingUserDetails);
            log.debug("Created Information for BookingUserDetails: {}", bookingUserDetails);
            return bookingUserDetailsMapper.toDto(bookingUserDetails);
    	} else {
    		throw new IllegalArgumentException("User login does not exist");
    	}
    }
}
