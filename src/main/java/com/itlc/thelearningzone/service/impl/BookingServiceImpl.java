package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.BookingService;
import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.domain.Resource;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.repository.AuthorityRepository;
import com.itlc.thelearningzone.repository.BookingRepository;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.service.dto.BookingDTO;
import com.itlc.thelearningzone.service.dto.UserInfoDTO;
import com.itlc.thelearningzone.service.dto.NotificationDTO;
import com.itlc.thelearningzone.service.mapper.BookingMapper;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.service.MailService;
import com.itlc.thelearningzone.service.NotificationService;
import com.itlc.thelearningzone.service.UserService;
import com.itlc.thelearningzone.service.ResourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ArrayList;


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
	
	private final ResourceService resourceService;

	private final NotificationService notificationService;
	
	private final Long ADMIN_ID = (long) 8; // be sure admin has a userInfo id otherwise constraint violation when creating a notification
	
    private final String SENDER_URL = "../../content/images/";
	
	private final String IMAGE_FORMAT = ".png";
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK).withZone(ZoneId.systemDefault());

	public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
			UserRepository userRepository, MailService mailService, ResourceService resourceService, NotificationService notificationService, UserInfoRepository userInfoRepository,UserService userService) {
		this.bookingRepository = bookingRepository;
		this.bookingMapper = bookingMapper;
		this.userRepository = userRepository;
		this.mailService = mailService;
		this.resourceService = resourceService;
		this.notificationService = notificationService;
		this.userService = userService;

	}

	@Override
	public Page<BookingDTO> findAllInTimeFrame(Pageable pageable, Instant startTime, Instant endTime) {
		log.debug("Request to get all Bookings between {} and {}", Date.from(startTime), Date.from(endTime));
		return bookingRepository.findAllInTimeFrame(pageable, startTime, endTime).map(bookingMapper::toDto);
	}
	
	@Override
	public Page<BookingDTO> findUserBookingsInTimeFrame(Pageable pageable, Long userId, Instant startTime, Instant endTime) {
		log.debug("Request to get all Bookings between {} and {} for user {}", Date.from(startTime), Date.from(endTime), userId);
		return bookingRepository.findUserBookingsInTimeFrame(pageable, userId, startTime, endTime).map(bookingMapper::toDto);
	}
	
	@Override
	public Page<BookingDTO> findUserBookings(Pageable pageable, Long userId) {
		log.debug("Request to get all Bookings for user {}", userId);
		return bookingRepository.findUserBookings(pageable, userId).map(bookingMapper::toDto);
	}
	
	@Override
    public Page<BookingDTO> findUserBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime) {
		log.debug("Request to get all Bookings modified after {0} for user {1}", Date.from(startTime), userId);
		return bookingRepository.findUserBookingsModifiedAfterTime(pageable, userId, startTime).map(bookingMapper::toDto);
	}
    
    @Override
    public Page<BookingDTO> findBookingsModifiedAfterTime(Pageable pageable, Instant startTime) {
    	log.debug("Request to get all Bookings modified after {}", Date.from(startTime));
    	return bookingRepository.findBookingsModifiedAfterTime(pageable, startTime).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findTutorBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime) {
    	log.debug("Request to get all tutor Bookings modified after {} for user {}", Date.from(startTime), userId);
    	return bookingRepository.findTutorBookingsModifiedAfterTime(pageable, Math.toIntExact(userId), startTime).map(bookingMapper::toDto);
    }
	
    @Override
    public Page<BookingDTO> findTutorPendingRequestsBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime) {
    	log.debug("Request to get tutor Bookings pending tutor's acceptance after {} for user {}", Date.from(startTime), userId);
    	return bookingRepository.findTutorPendingRequestsBookingsModifiedAfterTime(pageable, Math.toIntExact(userId), startTime).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findTutorBookings(Pageable pageable, Long userId) {
    	log.debug("Request to get tutor Bookings for user {}", userId);
    	return bookingRepository.findTutorBookings(pageable, Math.toIntExact(userId)).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findTutorPendingRequestsBookings(Pageable pageable, Long userId) {
    	log.debug("Request to get tutor pending Bookings for user {}", userId);
    	return bookingRepository.findTutorPendingRequestsBookings(pageable, Math.toIntExact(userId)).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findConfirmedInTimeFrame(Pageable pageable, Instant startTime, Instant endTime) {
    	log.debug("Request to get confirmed Bookings between {} and {}", Date.from(startTime), Date.from(endTime));
    	return bookingRepository.findConfirmedInTimeFrame(pageable, startTime, endTime).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findConfirmedUserBookingsInTimeFrame(Pageable pageable, Long userId, Instant startTime, Instant endTime) {
    	log.debug("Request to get confirmed Bookings between {} and {} for user {}", Date.from(startTime), Date.from(endTime), userId);
    	return bookingRepository.findConfirmedUserBookingsInTimeFrame(pageable, userId, startTime, endTime).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findUserConfirmedBookings(Pageable pageable, Long userId) {
    	log.debug("Request to get confirmed Bookings for user {}", userId);
    	return bookingRepository.findUserConfirmedBookings(pageable, userId).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findConfirmedBookings(Pageable pageable) {
    	log.debug("Request to get confirmed Bookings");
    	return bookingRepository.findConfirmedBookings(pageable).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findConfirmedBookingsModifiedAfterTime(Pageable pageable, Instant startTime) {
    	log.debug("Request to get confirmed Bookings after {}", Date.from(startTime));
    	return bookingRepository.findConfirmedBookingsModifiedAfterTime(pageable, startTime).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findUserConfirmedBookingsModifiedAfterTime(Pageable pageable, Long userId, Instant startTime) {
    	log.debug("Request to get confirmed Bookings after {} for user {}", Date.from(startTime), userId);
    	return bookingRepository.findUserConfirmedBookingsModifiedAfterTime(pageable, userId, startTime).map(bookingMapper::toDto);
    }
    
    @Override
    public Page<BookingDTO> findBookingsPendingAdminApproval(Pageable pageable) {
    	log.debug("Request to get Bookings not accepted by an admin");
    	return bookingRepository.findBookingsPendingAdminApproval(pageable).map(bookingMapper::toDto);
    }

    @Override
    public Page<BookingDTO> findBookingsPendingAdminApprovalModifiedAfterTime(Pageable pageable, Instant startTime) {
    	log.debug("Request to get Bookings not accepted by an admin after {}", Date.from(startTime));
    	return bookingRepository.findBookingsPendingAdminApprovalChanges(pageable, startTime).map(bookingMapper::toDto);
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
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
				
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public void saveBookingWithAdminNotification(@Valid BookingDTO bookingDTO) {

		// booking updated so set modifiedTimestamp
		bookingDTO.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
				
		// Creating a notification for the admin that a student has requested a booking.
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		notification.setRead(false);
		// getting sender 
		Optional<User> sender = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
		for(UserInfoDTO userInfoDTO : bookingDTO.getUserInfos()) {
			notification.setSenderId(userInfoDTO.getId());
		}
		// setting message
		String notificationMessage = "New booking request on ".concat(bookingDTO.getTitle().concat(" from ").concat(sender.get().getFirstName().concat(" ").concat(sender.get().getLastName())));
		notification.setMessage(notificationMessage);
		notification.setBookingId(bookingDTO.getId());
		notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		// getting receiver 
         Optional<User> receiver = userRepository.findById(ADMIN_ID);
         notification.setReceiverId(receiver.get().getId());
		 notificationService.save(notification);
				
	}

	@Override
	public BookingDTO updateBookingCancelled(@Valid BookingDTO bookingDTO) {

		for (UserInfoDTO userInfoDTO : bookingDTO.getUserInfos()) {

			// Sending a cancellation of booking Email to every user that registered for the booking
			Long id = userInfoDTO.getUserId();
			User user = userRepository.getOne(id);
			Integer idTut = bookingDTO.getTutorAcceptedId();
			Long tutorID = Long.valueOf(idTut.longValue());
			User tutorUser = userRepository.getOne(tutorID);
//			Booking booking = bookingMapper.toEntity(bookingDTO);
			String langKey = "en";
			user.setLangKey(langKey);
//			mailService.sendBookingCancelledEmail(booking, user, tutorUser);

			// Creating the booking cancellation notifications for all the users that registered for the booking
			String date = formatter.format(bookingDTO.getStartTime());
			String notificationMessage = "Your booking on ".concat(" ").concat(bookingDTO.getTitle().concat(" at ").concat(date).concat(" has been cancelled"));
			NotificationDTO notification = new NotificationDTO();
			Instant instant = Instant.now();
			notification.setTimestamp(instant);
			notification.setMessage(notificationMessage);
			notification.setSenderImageURL(SENDER_URL.concat(tutorUser.getLogin()).concat(IMAGE_FORMAT));
			notification.setRead(false);
			notification.setSenderId(tutorID);
			notification.setReceiverId(userInfoDTO.getUserId());
			notification.setBookingId(bookingDTO.getId());
			notificationService.save(notification);
		}
		Booking booking = bookingMapper.toEntity(bookingDTO);
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
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
		// Long id = bookingDTO.getTutorAcceptedId();
		// Long tutorID = Long.valueOf(id.longValue());
		// User tutorUser = userRepository.getOne(tutorID);
		// Booking booking = bookingMapper.toEntity(bookingDTO);
		// String langKey = "en";
		// user.setLangKey(langKey);
		// mailService.sendBookingAcceptedByTutorEmail(booking, user, tutorUser);
		
		
		// Creating the booking acceptance notification for the user that requested the booking.	
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		notification.setRead(false);
		// getting sender id
		Integer idTut = bookingDTO.getTutorAcceptedId();
		Long tutorID = Long.valueOf(idTut.longValue());
		User tutorUser = userRepository.getOne(tutorID);
		notification.setSenderId(tutorID);
		notification.setSenderImageURL(SENDER_URL.concat(tutorUser.getLogin()).concat(IMAGE_FORMAT));
		// setting the message
		String date = formatter.format(bookingDTO.getStartTime());
		String notificationMessage = "Your booking request ".concat(bookingDTO.getTitle().concat(" on ").concat(date).concat(" with ").concat(tutorUser.getFirstName().concat(tutorUser.getLastName().concat(" has been accepted"))));
		notification.setMessage(notificationMessage);
		// getting receiverID
		Optional<User> reveiver = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
		notification.setReceiverId(reveiver.get().getId());
		notification.setBookingId(bookingDTO.getId());
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);

	}
	
	@Override
	public BookingDTO updateBookingAssignedTutor(@Valid BookingDTO bookingDTO) {
		
		// Creating a notification for the tutor that the admin has assigned a booking to him/her
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		notification.setRead(false);
		// getting sender 
		Optional<User> sender = userRepository.findById(ADMIN_ID);
		notification.setSenderId(ADMIN_ID);
		notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		// setting the notification message
		String date = formatter.format(bookingDTO.getStartTime());
		String notificationMessage = "New booking offer on ".concat(bookingDTO.getTitle().concat(" by student ").concat(bookingDTO.getRequestedBy().concat(" on ").concat(date)));
		notification.setMessage(notificationMessage);
		// getting receiver
		Integer idTut = bookingDTO.getTutorAcceptedId();
		Long tutorID = Long.valueOf(idTut.longValue());
		notification.setReceiverId(tutorID);
		notification.setBookingId(bookingDTO.getId());
		
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public BookingDTO updateBookingRejectedByTutor(@Valid BookingDTO bookingDTO) {
		
		// Creating a notification for the admin that a tutor has rejected a booking
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		notification.setRead(false);
		
		// getting sender
		Integer idTut = bookingDTO.getTutorAcceptedId();
		Long tutorID = Long.valueOf(idTut.longValue());
		Optional<User> sender = userRepository.findById(tutorID);
		notification.setSenderId(tutorID);
		notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		notification.setBookingId(bookingDTO.getId());
		// setting the notification message
		String notificationMessage = "".concat(bookingDTO.getTitle().concat(" offer rejected ").concat(" by ").concat(sender.get().getFirstName().concat(" ").concat(sender.get().getLastName())));
		notification.setMessage(notificationMessage);
		// getting receiver
		Optional<User> receiver = userRepository.findById(ADMIN_ID);
		notification.setReceiverId(receiver.get().getId());
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public BookingDTO updateBookingRequestRejectedByAdmin(@Valid BookingDTO bookingDTO) {
		
		// Creating a notification for the student that requested a booking that there request is rejected. notification comes from the admin
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		notification.setRead(false);
				
		// getting sender
		Optional<User> sender = userRepository.findById(ADMIN_ID);
		notification.setSenderId(sender.get().getId());
		notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		// getting receiver
		Optional<User> reveiver = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
		notification.setReceiverId(reveiver.get().getId());
		notification.setBookingId(bookingDTO.getId());
		
		// setting the notification message
		String notificationMessage = "Sorry ".concat(reveiver.get().getFirstName()).concat(", there are no bookings on ").concat(bookingDTO.getTitle().concat(" based on the times you selected. Please request again"));;
		notification.setMessage(notificationMessage);
		
		notificationService.save(notification);
		
		//sending email to student
		Booking booking = bookingMapper.toEntity(bookingDTO);
//		User student = userRepository.getOne(reveiver.get().getId());
//		mailService.sendBookingRequestRejectedByAdminEmail(booking, student);
		
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
		booking = bookingRepository.save(booking);
		
		/* Begin email notification */
		List<Resource> resources = new ArrayList<>();
		
		// Booking not be a booking request; no subject
		if (booking.getSubject().getId() != null) {
		resources = resourceService.findAllResourcesInSubject(booking.getSubject().getId());
		}
		// Send booking rejected email to user
		mailService.sendBookingRejectedEmail(booking, booking.getUserInfos(), resources);
		
		return bookingMapper.toDto(booking);
	}
	
	
	
	@Override
	public List<BookingDTO> findAllBookingsList(Instant instantFromDate, Instant instantToDate) {
		List<BookingDTO> list = new ArrayList<BookingDTO>();
		List<Booking> ps = bookingRepository.findAllWithBookingUserDetails(instantFromDate, instantToDate);
		for (Booking p : ps)
			list.add(bookingMapper.toDto(p));

		return list;
	}
	
	@Override
	public List<BookingDTO> findAllBookingsDistributionList(Instant instantFromDate, Instant instantToDate) {
		List<BookingDTO> list = new ArrayList<BookingDTO>();
		List<Booking> ps = bookingRepository.findAllWithoutBookingUserDetails(instantFromDate, instantToDate);
		for (Booking p : ps)
			list.add(bookingMapper.toDto(p));

		return list;
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
