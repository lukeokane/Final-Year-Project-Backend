package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.BookingService;
import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.domain.BookingUserDetails;
import com.itlc.thelearningzone.domain.Resource;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.domain.UserInfo;
import com.itlc.thelearningzone.repository.BookingRepository;
import com.itlc.thelearningzone.repository.ResourceRepository;
import com.itlc.thelearningzone.repository.BookingUserDetailsRepository;
import com.itlc.thelearningzone.service.dto.BookingDTO;
import com.itlc.thelearningzone.service.dto.MessageDTO;
import com.itlc.thelearningzone.service.dto.UserInfoDTO;
import com.itlc.thelearningzone.service.dto.NotificationDTO;
import com.itlc.thelearningzone.service.mapper.BookingMapper;
import com.itlc.thelearningzone.service.mapper.MessageMapper;
import com.itlc.thelearningzone.web.rest.errors.BadRequestAlertException;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.service.MailService;
import com.itlc.thelearningzone.service.NotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
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
	
	private final MessageMapper messageMapper;

	private final UserRepository userRepository;
	
	private final BookingUserDetailsRepository bookingUserDetailsRepository;

	private final MailService mailService;
	
	private final ResourceRepository resourceRepository;

	private final NotificationService notificationService;
	
	private static final Long ADMIN_ID = 8L; // be sure admin has a userInfo id otherwise constraint violation when creating a notification
	
    private static final String SENDER_URL = "../../content/images/";
	
	private static final String IMAGE_FORMAT = ".png";
	
	private static final String ENTITY_NAME = "BookingService";
	
	private static final String ID_NULL = "idnull";
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK).withZone(ZoneId.systemDefault());

	public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, MessageMapper messageMapper,
			UserRepository userRepository, BookingUserDetailsRepository bookingUserDetailsRepository, MailService mailService, ResourceRepository resourceRepository, NotificationService notificationService) {
		this.bookingRepository = bookingRepository;
		this.bookingMapper = bookingMapper;
		this.messageMapper = messageMapper;
		this.userRepository = userRepository;
		this.bookingUserDetailsRepository = bookingUserDetailsRepository;
		this.mailService = mailService;
		this.resourceRepository = resourceRepository;
		this.notificationService = notificationService;
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
		log.debug("Request to get all Bookings modified after {} for user {}", Date.from(startTime), userId);
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
		
		// if a new booking, create booking user details for all users 
		if (booking.getId() == null && booking.getUserInfos().size() > 0) {
			Set<BookingUserDetails> bookingUserDetails = new HashSet<BookingUserDetails>();
			for (UserInfo userInfo : booking.getUserInfos()) {
				BookingUserDetails bookingUserDetail = new BookingUserDetails();
				bookingUserDetail.setBooking(booking);
				bookingUserDetail.setUserInfo(userInfo);
				
				bookingUserDetailsRepository.save(bookingUserDetail);
				bookingUserDetails.add(bookingUserDetail);
			}
			booking.setBookingUserDetails(bookingUserDetails);
		}
		
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public BookingDTO updateBooking(@Valid BookingDTO editedBookingDTO, MessageDTO message) {
		log.debug("Request to edit booking and send notifications: {}", editedBookingDTO);
		
		// Get the current Booking entity saved in the database
		Booking currentBooking = bookingRepository.findById(editedBookingDTO.getId()).orElseThrow(() -> new BadRequestAlertException("Booking with id " + editedBookingDTO.getId() + " does not exist", ENTITY_NAME, ID_NULL));

		if ((currentBooking.isCancelled() && currentBooking.getAdminAcceptedId() == null) && (!editedBookingDTO.isCancelled() && editedBookingDTO.getAdminAcceptedId() != null)) {
			log.debug("Booking went from rejected to confirmed");
			
			List<Resource> resources = new ArrayList<>();
			// Get resources for topics
			if (currentBooking.getTopics().size() > 0) {
				resources = resourceRepository.findAllResourcesInBooking(currentBooking.getId());
			}
			
			User tutor = null;
			if (editedBookingDTO.getTutorAcceptedId() != null) {
				tutor = userRepository.findById((long) editedBookingDTO.getTutorAcceptedId()).orElseThrow(() -> new BadRequestAlertException("Tutor with id " + editedBookingDTO.getTutorAcceptedId() + " does not exist", ENTITY_NAME, ID_NULL));
			}					
			
			bookingRepository.save(bookingMapper.toEntity(editedBookingDTO));
			
			// Send confirmed booking email
			mailService.sendBookingConfirmedEmail(currentBooking, currentBooking.getUserInfos(), tutor, resources);
		}
		else if ((!currentBooking.isCancelled() && currentBooking.getAdminAcceptedId() == null) && (!editedBookingDTO.isCancelled() && editedBookingDTO.getAdminAcceptedId() != null)) {
			log.debug("Booking went from pending to confirmed");
			
			List<Resource> resources = new ArrayList<>();
			// Get resources for topics
			if (currentBooking.getTopics().size() > 0) {
				resources = resourceRepository.findAllResourcesInBooking(currentBooking.getId());
			}
			
			User tutor = null;
			if (editedBookingDTO.getTutorAcceptedId() != null) {
				tutor = userRepository.findById((long) editedBookingDTO.getTutorAcceptedId()).orElseThrow(() -> new BadRequestAlertException("Tutor with id " + editedBookingDTO.getTutorAcceptedId() + " does not exist", ENTITY_NAME, ID_NULL));
			}		
			
			bookingRepository.save(bookingMapper.toEntity(editedBookingDTO));
			
			mailService.sendBookingConfirmedEmail(bookingMapper.toEntity(editedBookingDTO), currentBooking.getUserInfos(), tutor, resources);
		}
		else if ((!currentBooking.isCancelled() && currentBooking.getAdminAcceptedId() == null) && (editedBookingDTO.isCancelled() && editedBookingDTO.getAdminAcceptedId() == null)) {
			log.debug("Booking went from pending to rejected");
			
			List<Resource> resources = new ArrayList<>();
			// Get resources for topics
			if (currentBooking.getTopics().size() > 0) {
				resources = resourceRepository.findAllResourcesInBooking(currentBooking.getId());
			}
			
			bookingRepository.save(bookingMapper.toEntity(editedBookingDTO));
			
			// Send confirmed booking email
			mailService.sendBookingNotPossibleEmail(currentBooking, currentBooking.getUserInfos(), resources, messageMapper.toEntity(message));
		}
		else if ((!currentBooking.isCancelled() && currentBooking.getAdminAcceptedId() != null) && (editedBookingDTO.isCancelled() && editedBookingDTO.getAdminAcceptedId() != null)) {
			log.debug("Booking went from confirmed to cancelled");
			
			List<Resource> resources = new ArrayList<>();
			// Get resources for topics
			if (currentBooking.getTopics().size() > 0) {
				resources = resourceRepository.findAllResourcesInBooking(currentBooking.getId());
			}
			
			bookingRepository.save(bookingMapper.toEntity(editedBookingDTO));
			
			// Send confirmed booking email
			mailService.sendBookingNotPossibleEmail(currentBooking, currentBooking.getUserInfos(), resources, messageMapper.toEntity(message));
		}
		else if ((currentBooking.isCancelled() && currentBooking.getAdminAcceptedId() != null) && (!editedBookingDTO.isCancelled() && editedBookingDTO.getAdminAcceptedId() != null)) {
			log.debug("Booking went from cancelled to confirmed");
			
			List<Resource> resources = new ArrayList<>();
			// Get resources for topics
			if (currentBooking.getTopics().size() > 0) {
				resources = resourceRepository.findAllResourcesInBooking(currentBooking.getId());
			}
			
			User tutor = null;
			if (editedBookingDTO.getTutorAcceptedId() != null) {
				tutor = userRepository.findById((long) editedBookingDTO.getTutorAcceptedId()).orElseThrow(() -> new BadRequestAlertException("Tutor with id " + editedBookingDTO.getTutorAcceptedId() + " does not exist", ENTITY_NAME, ID_NULL));
			}					

			bookingRepository.save(bookingMapper.toEntity(editedBookingDTO));
			
			// Send confirmed booking email
			mailService.sendBookingConfirmedEmail(currentBooking, currentBooking.getUserInfos(), tutor, resources);
		}
		else {
			log.debug("Booking was only edited");
			
			List<Resource> resources = new ArrayList<>();
			// Get resources for topics
			if (currentBooking.getTopics().size() > 0) {
				resources = resourceRepository.findAllResourcesInBooking(currentBooking.getId());
			}
			
			User oldTutor = null;
			User newTutor = null;
			if (editedBookingDTO.getTutorAcceptedId() != null) {
				oldTutor = userRepository.findById((long) currentBooking.getTutorAcceptedId()).orElseThrow(() -> new BadRequestAlertException("Tutor with id " + editedBookingDTO.getTutorAcceptedId() + " does not exist", ENTITY_NAME, ID_NULL));
				newTutor = userRepository.findById((long) editedBookingDTO.getTutorAcceptedId()).orElseThrow(() -> new BadRequestAlertException("Tutor with id " + editedBookingDTO.getTutorAcceptedId() + " does not exist", ENTITY_NAME, ID_NULL));
			}					
			Booking oldBooking = new Booking(currentBooking);
			bookingRepository.save(bookingMapper.toEntity(editedBookingDTO));
			// Send confirmed booking email
			mailService.sendBookingEditedEmail(oldBooking, currentBooking, currentBooking.getUserInfos(), oldTutor, newTutor);
		}		
		
		return editedBookingDTO;
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
//		// getting sender 
		Optional<User> sender = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
		for(UserInfoDTO userInfoDTO : bookingDTO.getUserInfos()) {
			notification.setSenderId(userInfoDTO.getId());
		}
		// setting message & sender image url
		if(sender.isPresent()) {	
		   String notificationMessage = "New booking request on ".concat(bookingDTO.getTitle().concat(" from ").concat(sender.get().getFirstName().concat(" ").concat(sender.get().getLastName())));
	       notification.setMessage(notificationMessage); 
	       notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		}
		notification.setBookingId(bookingDTO.getId());		
		// getting receiver 
           Optional<User> receiver = userRepository.findById(ADMIN_ID);
        if(receiver.isPresent()) {
           notification.setReceiverId(receiver.get().getId());
		}
		
		//notificationService.save(notification);
				
	}
	
	@Override
	public void updateBookingAcceptedTutorAssigned(Long bookingId, Integer adminId, Integer tutorId)
	{
		log.debug("REST request to update booking ID {}, accepted by admin ID {} and assigned to tutor ID {}", bookingId, adminId, tutorId);
		
		/* Check that booking, adminId and tutorId exist */
		Booking booking = bookingRepository.findOneWithEagerRelationships(bookingId).orElseThrow(() -> new BadRequestAlertException("Booking with id " + bookingId + " does not exist", ENTITY_NAME, ID_NULL));
		User admin = userRepository.findById(adminId.longValue()).orElseThrow(() -> new BadRequestAlertException("User id " + adminId + " is does not exist", ENTITY_NAME, ID_NULL));
		User tutor = userRepository.findById(tutorId.longValue()).orElseThrow(() -> new BadRequestAlertException("User id " + tutorId + " is does not exist", ENTITY_NAME, ID_NULL));

		/* Begin email notification */
		List<Resource> resources = new ArrayList<>();
		
		// Booking might not be a booking request; no subject
		if (booking.getSubject() != null) {
		resources = resourceRepository.findAllResourcesInBooking(booking.getId());
		}
		
		// Sonar indicates 'admin' property is unused, will get ID from admin instead of using the parameter to circumvent.
		booking.setAdminAcceptedId(admin.getId().intValue());
		booking.setTutorAccepted(true);
		booking.setTutorAcceptedId(tutorId);
		
		bookingRepository.save(booking);
		
		// Send booking confirmed email to user
		mailService.sendBookingConfirmedEmail(booking, booking.getUserInfos(), tutor, resources);
		
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
			String langKey = "en";
			user.setLangKey(langKey);

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
		Optional<User> receiver = userRepository.findOneByLogin(bookingDTO.getRequestedBy()); // using findByLogin to get receiverId of person who requested booking - requested by string provided																					
		if(receiver.isPresent()) {
		   notification.setReceiverId(receiver.get().getId());
		}
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
		if(sender.isPresent()) {
			notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		}
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
		// setting the notification message & sender imageUrl
		if(sender.isPresent()) {
			String notificationMessage = "".concat(bookingDTO.getTitle().concat(" offer rejected ").concat(" by ").concat(sender.get().getFirstName().concat(" ").concat(sender.get().getLastName())));
			notification.setMessage(notificationMessage);
		   notification.setSenderImageURL(SENDER_URL.concat(sender.get().getLogin()).concat(IMAGE_FORMAT));
		}
		notification.setBookingId(bookingDTO.getId());
		// getting receiver
		Optional<User> receiver = userRepository.findById(ADMIN_ID);
		if(receiver.isPresent()) {
		   notification.setReceiverId(receiver.get().getId());
		}
		notificationService.save(notification);
		
		Booking booking = bookingMapper.toEntity(bookingDTO);
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
		booking = bookingRepository.save(booking);
		return bookingMapper.toDto(booking);
	}
	
	@Override
	public BookingDTO updateBookingRequestRejectedByAdmin(@Valid BookingDTO bookingDTO, MessageDTO messageDTO) {
		
		// Creating a notification for the student that requested a booking that there request is rejected. notification comes from the admin
		NotificationDTO notification = new NotificationDTO();
		Instant instant = Instant.now();
		notification.setTimestamp(instant);
		notification.setRead(false);
				
		// getting sender
		Optional<User> sender = userRepository.findById(ADMIN_ID);
		if(sender.isPresent()) {
		  notification.setSenderId(sender.get().getId());
		  notification.setSenderImageURL(SENDER_URL + sender.get().getLogin() + IMAGE_FORMAT);
		}
		// getting receiver
		Optional<User> receiver = userRepository.findById(bookingDTO.getUserInfos().iterator().next().getId()); // using findByLogin to get receiverId of person who requested booking - requested by string provided	
		// setting the notification message
		if(receiver.isPresent()) {
			String notificationMessage = "Sorry " + receiver.get().getFirstName() + ", there are no bookings on " + bookingDTO.getTitle() + " based on the times you selected. Please request again";
			notification.setMessage(notificationMessage); 
			notification.setReceiverId(receiver.get().getId());
		}
	    notification.setBookingId(bookingDTO.getId());
		
		notificationService.save(notification);
		
		//sending email to student
		Booking booking = bookingMapper.toEntity(bookingDTO);
		
		// booking updated so set modifiedTimestamp
		booking.setModifiedTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS));
		
		booking = bookingRepository.save(booking);
		
		/* Begin email notification */
		List<Resource> resources = new ArrayList<>();
		
		// Booking might not be a booking request; no subject
		if (booking.getSubject().getId() != null) {
		resources = resourceRepository.findAllResourcesInBooking(booking.getId());
		}
		// Send booking rejected email to user
		mailService.sendBookingNotPossibleEmail(booking, booking.getUserInfos(), resources, messageMapper.toEntity(messageDTO));
		
		return bookingMapper.toDto(booking);
	}
	
	
	
	@Override
	public List<BookingDTO> findAllBookingsList(Instant instantFromDate, Instant instantToDate) {
		List<BookingDTO> list = new ArrayList<>();
		List<Booking> ps = bookingRepository.findAllBookings(instantFromDate, instantToDate);
		for (Booking p : ps)
			list.add(bookingMapper.toDto(p));

		return list;
	}
	
	
	@Override
	public List<BookingDTO> findAllBookingsAllCoursesSelectedYearBetweenDates(Instant instantFromDate,
			Instant instantToDate, Integer selectedYear) {
		List<BookingDTO> list = new ArrayList<>();
		List<Booking> ps = bookingRepository.findBookingsAllcoursesSelectedYear(instantFromDate, instantToDate, selectedYear);
		for (Booking p : ps)
			list.add(bookingMapper.toDto(p));

		return list;
	}
	
	@Override
	public List<BookingDTO> findAllBookingsSelectedCourseSelectedYearBetweenDates(Instant instantFromDate,
			Instant instantToDate, Integer courseId, Integer selectedYear) {
		Long lCourseId = Long.valueOf(courseId);
		List<BookingDTO> list = new ArrayList<>();
		List<Booking> ps = bookingRepository.findBookingsSelectedCourseSelectedYear(instantFromDate, instantToDate, lCourseId, selectedYear);
		for (Booking p : ps)
			list.add(bookingMapper.toDto(p));

		return list;
	
	}
	
	@Override
	public List<BookingDTO> findAllBookingsSelectedCourseAllYearsBetweenDates(Instant instantFromDate,
			Instant instantToDate, Integer courseId) {
		Long lCourseId = Long.valueOf(courseId);
		List<BookingDTO> list = new ArrayList<>();
		List<Booking> ps = bookingRepository.findBookingsSelectedCourseAllYears(instantFromDate, instantToDate, lCourseId);
		for (Booking p : ps)
			list.add(bookingMapper.toDto(p));

		return list;
	}
	
    /**
     * Send booking reminders to participants
	 *
     * This is scheduled to get fired everyday, at 18:00 (pm).
     */
	@Async
    @Scheduled(cron = "0 0 18 * * ?")
    public void sendBookingEveningReminderEmail() {
    	
    	// Get next day time and end of next day time
    	Instant currentTime = Instant.now();
    	Instant nextDay = currentTime.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
    	Instant endOfNextDay = nextDay.plus(1, ChronoUnit.DAYS);
    	
    	// Get bookings between those two times
    	List<Booking> bookings = bookingRepository.findConfirmedBookingsAfterStartTimeAndEndTimeInclusive(nextDay, endOfNextDay);
    	
    	log.debug("Scheduled task, runs at 18:00 every day: sending emails to attendees of {} bookings", bookings.size());
    	log.debug("Scheduled task @ 18:00 getting bookings from {} to {}", nextDay, endOfNextDay );
    	for (int i = 0; i < bookings.size(); i++) {
    		Booking booking = new Booking(bookings.get(i));
    		Optional<User> tutor = userRepository.findById(booking.getTutorAcceptedId().longValue());
    		if (tutor.isPresent()) {
    		// Send email to attendees
    			mailService.sendBookingReminderEmail(bookings.get(i), bookings.get(i).getUserInfos(), tutor.get(), "tomorrow");
    		}
    		else {
    			mailService.sendBookingReminderEmail(bookings.get(i), bookings.get(i).getUserInfos(), null, "tomorrow");
    		}
    	}
    }
    
    /**
     * Send booking reminders to participants an hour before booking commences
	 *
     * This is scheduled to get fired every 5 minutes
     */
    @Async
    @Scheduled(cron = "0 0/5 * * * ?")
    public void sendBookingHourBeforeReminderEmail() {
    	
    	// Get time an hour from now and an hour + 5 minutes from now
    	Instant currentTime = new Date().toInstant();

    	// Add daylight savings
    	Boolean DST = ZoneId.of(ZoneId.systemDefault().toString()).getRules().isDaylightSavings(Instant.now());
    	if (DST) {
    		//currentTime = currentTime.atOffset(ZoneOffset.ofHours(1)).toInstant();
    		currentTime = currentTime.plus(1, ChronoUnit.HOURS); 		
    	}
    	
    	Instant startTime = currentTime.plus(1, ChronoUnit.HOURS);
    	Instant endTime = startTime.plus(5, ChronoUnit.MINUTES);   	
    	
    	// Get bookings between those two times
    	List<Booking> bookings = bookingRepository.findConfirmedBookingsAfterStartTimeAndEndTimeInclusive(startTime, endTime);
    			
    	log.debug("Scheduled task, runs every 5 minutes: sending emails to attendees of {} bookings", bookings.size());
    	for (int i = 0; i < bookings.size(); i++) {
    		Booking booking = bookings.get(i);

    		Optional<User> tutor = userRepository.findById(booking.getTutorAcceptedId().longValue());
    		if (tutor.isPresent()) {
    			// Send email to attendees
    			mailService.sendBookingReminderEmail(bookings.get(i), bookings.get(i).getUserInfos(), tutor.get(), "today");
    		}
    		else {
    			mailService.sendBookingReminderEmail(bookings.get(i), bookings.get(i).getUserInfos(), null, "today");
    		}
    	}
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
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("Booking with id " + id + " does not exist", ENTITY_NAME, ID_NULL));
		// Delete BookingUserDetails foreign key constraints
		for (BookingUserDetails bud : booking.getBookingUserDetails()) {
			bookingUserDetailsRepository.deleteById(bud.getId());
		}
		
		bookingRepository.deleteById(id);
	}

	/**
     * Cancel the "bookingID" booking.
     *
     * @param bookingID the bookingID belonging to the booking entity to be deleted
     * @return the entity
     */
	@Override
	public BookingDTO cancelBooking(Long bookingID) {
		log.debug("Request to cancel Booking : {}", bookingID);
		
		Optional<Booking> booking = bookingRepository.findById(bookingID);
    	if (booking.isPresent()) 
    	{
    		Booking updatedBooking = bookingRepository.getOne(bookingID);
    		updatedBooking.setCancelled(true);
    		updatedBooking = bookingRepository.save(updatedBooking);
            log.debug("Created Information for Booking: {}", updatedBooking);
            return bookingMapper.toDto(updatedBooking);
    	} else {
    		throw new IllegalArgumentException("Booking with that ID does not exist");
    	}
	}


}