package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.BookingService;
import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.repository.AuthorityRepository;
import com.itlc.thelearningzone.repository.BookingRepository;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.service.dto.BookingDTO;
import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;
import com.itlc.thelearningzone.service.dto.UserInfoDTO;
import com.itlc.thelearningzone.service.dto.NotificationDTO;
import com.itlc.thelearningzone.service.mapper.BookingMapper;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.service.MailService;
import com.itlc.thelearningzone.service.NotificationService;
import com.itlc.thelearningzone.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * Service Implementation for managing Booking.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

	private final BookingRepository bookingRepository;

	private final BookingMapper bookingMapper;

	private final UserRepository userRepository;
	
	private final UserService userService;

	private final MailService mailService;

	private final NotificationService notificationService;
	
	private final Long ADMIN_ID = (long) 9; // be sure admin has a userInfo id otherwise constraint violation when creating a notification

	public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
			UserRepository userRepository, MailService mailService, NotificationService notificationService, UserInfoRepository userInfoRepository,UserService userService) {
		this.bookingRepository = bookingRepository;
		this.bookingMapper = bookingMapper;
		this.userRepository = userRepository;
		this.mailService = mailService;
		this.notificationService = notificationService;
		this.userService = userService;

	}

	/**
	 * Save a booking.
	 *
	 * @param bookingDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public BookingDTO save(BookingDTO bookingDTO) {
		log.debug("Request to save Booking : {}", bookingDTO);
		Booking booking = bookingMapper.toEntity(bookingDTO);
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public void saveBookingWithAdminNotification(@Valid BookingDTO bookingDTO) {
		
		//Optional<User> users = userRepository.findByAuthority("marcus@live.ie");
		// Creating a notification for the admin that a student has requested a tutorial.
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		String notificationMessage = "New tutorial request";
		notification.setMessage(notificationMessage);
		notification.setRead(false);
		// getting sender 
		Optional<User> sender = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
//		notification.setSenderId(sender.get().getId());
		for(UserInfoDTO userInfoDTO : bookingDTO.getUserInfos()) {
			notification.setSenderId(userInfoDTO.getId());
		}		
		notification.setBookingId(bookingDTO.getId());
		notification.setSenderImageURL(sender.get().getImageUrl());
		// getting receiver 
		 //need to find a way to get the admin ID
         Optional<User> receiver = userRepository.findById(ADMIN_ID);
         notification.setReceiverId(receiver.get().getId());
		 notificationService.save(notification);
				
	}

	@Override
	public BookingDTO updateBookingCancelled(@Valid BookingDTO bookingDTO) {

		for (UserInfoDTO userInfoDTO : bookingDTO.getUserInfos()) {

			// Sending a cancellation of tutorial Email to every user that registered for the tutorial
			Long id = userInfoDTO.getUserId();
			User user = userRepository.getOne(id);
			Integer idTut = bookingDTO.getTutorAcceptedId();
			Long tutorID = Long.valueOf(idTut.longValue());
			User tutorUser = userRepository.getOne(tutorID);
//			Booking booking = bookingMapper.toEntity(bookingDTO);
			String langKey = "en";
			user.setLangKey(langKey);
//			mailService.sendBookingCancelledEmail(booking, user, tutorUser);

			// Creating the tutorial cancellation notifications for all the users that registered for the tutorial
			String notificationMessage = "Your tutorial has been cancelled";
			NotificationDTO notification = new NotificationDTO();
			Instant instant = Instant.now();
			notification.setTimestamp(instant);
			notification.setMessage(notificationMessage);
			notification.setSenderImageURL(tutorUser.getImageUrl());
			notification.setRead(false);
			notification.setSenderId(tutorID);
			notification.setReceiverId(userInfoDTO.getUserId());
			notification.setBookingId(bookingDTO.getId());
			notificationService.save(notification);
		}
		Booking booking = bookingMapper.toEntity(bookingDTO);
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);

	}

	@Override
	public BookingDTO updateBookingAccepted(@Valid BookingDTO bookingDTO) {
		
		// When a tutor accepts a request from the ITLC Admin, A confirmation Email is
		// sent to the itlc user who requesting the booking
		// BookingUserDetailsDTO bookingUserDetailsDTO =
		// bookingDTO.getBookingUserDetailsDTO().iterator().next();
		// User user = userRepository.getOne(bookingUserDetailsDTO.getId());
		// Integer id = bookingDTO.getTutorAcceptedId();
		// Long tutorID = Long.valueOf(id.longValue());
		// User tutorUser = userRepository.getOne(tutorID);
		// Booking booking = bookingMapper.toEntity(bookingDTO);
		// String langKey = "en";
		// user.setLangKey(langKey);
		// mailService.sendBookingAcceptedByTutorEmail(booking, user, tutorUser);
		
		
		// Creating the tutorial acceptance notification for the user that requested the tutorial.	
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		String notificationMessage = "Your tutorial request has been accepted";
		notification.setMessage(notificationMessage);
		notification.setRead(false);
		// getting sender id
		Integer idTut = bookingDTO.getTutorAcceptedId();
		Long tutorID = Long.valueOf(idTut.longValue());
		User tutorUser = userRepository.getOne(tutorID);
		notification.setSenderId(tutorID);
		notification.setSenderImageURL(tutorUser.getImageUrl());
		// getting receiverID
		Optional<User> reveiver = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
		notification.setReceiverId(reveiver.get().getId());
		notification.setBookingId(bookingDTO.getId());
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);

	}
	
	@Override
	public BookingDTO updateBookingAssignedTutor(@Valid BookingDTO bookingDTO) {
		
		// Creating a notification for the tutor that the admin has assigned a tutorial to him/her
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		String notificationMessage = "New tutorial offer";
		notification.setMessage(notificationMessage);
		notification.setRead(false);
		// getting sender 
		// need to find a way to set the adminsID
		Optional<User> sender = userRepository.findById(ADMIN_ID);
		notification.setSenderId(ADMIN_ID);
		notification.setSenderImageURL(sender.get().getImageUrl());
		// getting receiver
		Integer idTut = bookingDTO.getTutorAcceptedId();
		Long tutorID = Long.valueOf(idTut.longValue());
		notification.setReceiverId(tutorID);
		notification.setBookingId(bookingDTO.getId());
		
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public BookingDTO updateBookingRejected(@Valid BookingDTO bookingDTO) {
		
		// Creating a notification for the admin that a tutor has rejected a tutorial
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		String notificationMessage = "Tutorial offer rejected";
		notification.setMessage(notificationMessage);
		notification.setRead(false);
		
		// getting sender
		Integer idTut = bookingDTO.getTutorAcceptedId();
		Long tutorID = Long.valueOf(idTut.longValue());
		Optional<User> sender = userRepository.findById(tutorID);
		notification.setSenderId(tutorID);
		notification.setSenderImageURL(sender.get().getImageUrl());
		notification.setBookingId(bookingDTO.getId());
		// getting receiver
		Optional<User> receiver = userRepository.findById(ADMIN_ID);
		notification.setReceiverId(receiver.get().getId());
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}


	/**
	 * Get all the bookings.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<BookingDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Bookings");
		return bookingRepository.findAll(pageable).map(bookingMapper::toDto);
	}

	/**
	 * Get all the Booking with eager load of many-to-many relationships.
	 *
	 * @return the list of entities
	 */
	public Page<BookingDTO> findAllWithEagerRelationships(Pageable pageable) {
		return bookingRepository.findAllWithEagerRelationships(pageable).map(bookingMapper::toDto);
	}

	/**
	 * Get one booking by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<BookingDTO> findOne(Long id) {
		log.debug("Request to get Booking : {}", id);
		return bookingRepository.findOneWithEagerRelationships(id).map(bookingMapper::toDto);
	}

	/**
	 * Delete the booking by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Booking : {}", id);
		bookingRepository.deleteById(id);
	}

	

	
	

}
