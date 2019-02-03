package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.BookingUserDetailsService;
import com.itlc.thelearningzone.domain.BookingUserDetails;
import com.itlc.thelearningzone.repository.BookingUserDetailsRepository;
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

    public BookingUserDetailsServiceImpl(BookingUserDetailsRepository bookingUserDetailsRepository, BookingUserDetailsMapper bookingUserDetailsMapper) {
        this.bookingUserDetailsRepository = bookingUserDetailsRepository;
        this.bookingUserDetailsMapper = bookingUserDetailsMapper;
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
		Set<BookingUserDetailsDTO> list = new HashSet<BookingUserDetailsDTO>();
		Set<BookingUserDetails> ps = bookingUserDetailsRepository.findAlltest(id);
		for (BookingUserDetails p : ps)
			list.add(bookingUserDetailsMapper.toDto(p));
		return list;
	}
}
