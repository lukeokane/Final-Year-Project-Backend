package com.itlc.thelearningzone.web.rest;

import com.itlc.thelearningzone.ThelearningzoneApp;

import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.domain.UserInfo;
import com.itlc.thelearningzone.repository.BookingRepository;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.service.BookingService;
import com.itlc.thelearningzone.service.SubjectService;
import com.itlc.thelearningzone.service.BookingUserDetailsService;
import com.itlc.thelearningzone.service.dto.BookingDTO;
import com.itlc.thelearningzone.service.mapper.BookingMapper;
import com.itlc.thelearningzone.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itlc.thelearningzone.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;

/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class BookingResourceIntTest {

	private static final String DEFAULT_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_TITLE = "BBBBBBBBBB";

	private static final String DEFAULT_REQUESTED_BY = "AAAAAAAAAA";
	private static final String UPDATED_REQUESTED_BY = "BBBBBBBBBB";

	private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
	private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

	private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
	private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

	private static final String DEFAULT_USER_COMMENTS = "AAAAAAAAAA";
	private static final String UPDATED_USER_COMMENTS = "BBBBBBBBBB";

	private static final OrdinalScale DEFAULT_IMPORTANCE_LEVEL = OrdinalScale.NONE;
	private static final OrdinalScale UPDATED_IMPORTANCE_LEVEL = OrdinalScale.LOW;

	private static final Integer DEFAULT_ADMIN_ACCEPTED_ID = 1;
	private static final Integer UPDATED_ADMIN_ACCEPTED_ID = 2;

	private static final Boolean DEFAULT_TUTOR_ACCEPTED = false;
	private static final Boolean UPDATED_TUTOR_ACCEPTED = true;

	private static final Integer DEFAULT_TUTOR_ACCEPTED_ID = 1;
	private static final Integer UPDATED_TUTOR_ACCEPTED_ID = 2;

	private static final Instant DEFAULT_MODIFIED_TIMESTAMP = Instant.ofEpochMilli(0L);
	private static final Instant UPDATED_MODIFIED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

	private static final Integer DEFAULT_TUTOR_REJECTED_COUNT = 1;
	private static final Integer UPDATED_TUTOR_REJECTED_COUNT = 2;

	private static final Boolean DEFAULT_CANCELLED = false;
	private static final Boolean UPDATED_CANCELLED = true;
	
	private static final String DEFAULT_REQUEST_TIMES = "AAAAAAAAAA";
	private static final String UPDATED_REQUEST_TIMES = "BBBBBBBBBB";
	
	private static final Boolean DEFAULT_READ_BY_ADMIN = false;
	private static final Boolean UPDATED_READ_BY_ADMIN = true;

	@Autowired
	private BookingRepository bookingRepository;

	@Mock
	private BookingRepository bookingRepositoryMock;

	@Autowired
	private BookingMapper bookingMapper;

	@Mock
	private BookingService bookingServiceMock;

	@Autowired
	private BookingService bookingService;
	
	@Mock
	private BookingUserDetailsService bookingUserDetailsService;

	@Mock
	private SubjectService subjectServiceMock;

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restBookingMockMvc;

	private Booking booking;

	private UserInfo userInfo;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final BookingResource bookingResource = new BookingResource(bookingService, subjectService, bookingUserDetailsService);
		this.restBookingMockMvc = MockMvcBuilders
			.standaloneSetup(bookingResource)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.setControllerAdvice(exceptionTranslator)
			.setConversionService(createFormattingConversionService())
			.setMessageConverters(jacksonMessageConverter)
			.build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Booking createEntity(EntityManager em) {
		Booking booking = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);
		
		return booking;
	}

	public static UserInfo createUserInfoEntity(EntityManager em) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(8L);

		return userInfo;
	}

	@Before
	public void initTest() {
		booking = createEntity(em);
		userInfo = createUserInfoEntity(em);
	}

	@Test
	@Transactional
	public void createBooking() throws Exception {
		int databaseSizeBeforeCreate = bookingRepository.findAll().size();

		// Create the Booking
		BookingDTO bookingDTO = bookingMapper.toDto(booking);
		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isCreated());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(DEFAULT_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(DEFAULT_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(DEFAULT_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(DEFAULT_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(DEFAULT_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(DEFAULT_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(DEFAULT_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(DEFAULT_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(DEFAULT_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(DEFAULT_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(DEFAULT_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(DEFAULT_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(DEFAULT_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void createBookingWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = bookingRepository.findAll().size();

		// Create the Booking with an existing ID
		booking.setId(1L);
		BookingDTO bookingDTO = bookingMapper.toDto(booking);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isBadRequest());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkTitleIsRequired() throws Exception {
		int databaseSizeBeforeTest = bookingRepository.findAll().size();
		// set the field null
		booking.setTitle(null);

		// Create the Booking, which fails.
		BookingDTO bookingDTO = bookingMapper.toDto(booking);

		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isBadRequest());

		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkRequestedByIsRequired() throws Exception {
		int databaseSizeBeforeTest = bookingRepository.findAll().size();
		// set the field null
		booking.setRequestedBy(null);

		// Create the Booking, which fails.
		BookingDTO bookingDTO = bookingMapper.toDto(booking);

		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isBadRequest());

		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkStartTimeIsRequired() throws Exception {
		int databaseSizeBeforeTest = bookingRepository.findAll().size();
		// set the field null
		booking.setStartTime(null);

		// Create the Booking, which fails.
		BookingDTO bookingDTO = bookingMapper.toDto(booking);

		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isBadRequest());

		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkEndTimeIsRequired() throws Exception {
		int databaseSizeBeforeTest = bookingRepository.findAll().size();
		// set the field null
		booking.setEndTime(null);

		// Create the Booking, which fails.
		BookingDTO bookingDTO = bookingMapper.toDto(booking);

		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isBadRequest());

		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkImportanceLevelIsRequired() throws Exception {
		int databaseSizeBeforeTest = bookingRepository.findAll().size();
		// set the field null
		booking.setImportanceLevel(null);

		// Create the Booking, which fails.
		BookingDTO bookingDTO = bookingMapper.toDto(booking);

		restBookingMockMvc
			.perform(post("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isBadRequest());

		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllBookings() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Get all the bookingList
		restBookingMockMvc
			.perform(get("/api/bookings?sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())));
	}

	@SuppressWarnings({ "unchecked" })
	public void getAllBookingsWithEagerRelationshipsIsEnabled() throws Exception {
		BookingResource bookingResource = new BookingResource(bookingServiceMock, subjectService, bookingUserDetailsService);
		when(bookingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

		MockMvc restBookingMockMvc = MockMvcBuilders
			.standaloneSetup(bookingResource)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.setControllerAdvice(exceptionTranslator)
			.setConversionService(createFormattingConversionService())
			.setMessageConverters(jacksonMessageConverter)
			.build();

		restBookingMockMvc.perform(get("/api/bookings?eagerload=true")).andExpect(status().isOk());

		verify(bookingServiceMock, times(1)).findAllWithEagerRelationships(any());
	}

	@SuppressWarnings({ "unchecked" })
	public void getAllBookingsWithEagerRelationshipsIsNotEnabled() throws Exception {
		BookingResource bookingResource = new BookingResource(bookingServiceMock, subjectService, bookingUserDetailsService);
		when(bookingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
		MockMvc restBookingMockMvc = MockMvcBuilders
			.standaloneSetup(bookingResource)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.setControllerAdvice(exceptionTranslator)
			.setConversionService(createFormattingConversionService())
			.setMessageConverters(jacksonMessageConverter)
			.build();

		restBookingMockMvc.perform(get("/api/bookings?eagerload=true")).andExpect(status().isOk());

		verify(bookingServiceMock, times(1)).findAllWithEagerRelationships(any());
	}

	@Test
	@Transactional
	public void getBooking() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Get the booking
		restBookingMockMvc
			.perform(get("/api/bookings/{id}", booking.getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.id").value(booking.getId().intValue()))
			.andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
			.andExpect(jsonPath("$.requestedBy").value(DEFAULT_REQUESTED_BY.toString()))
			.andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
			.andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
			.andExpect(jsonPath("$.userComments").value(DEFAULT_USER_COMMENTS.toString()))
			.andExpect(jsonPath("$.importanceLevel").value(DEFAULT_IMPORTANCE_LEVEL.toString()))
			.andExpect(jsonPath("$.adminAcceptedId").value(DEFAULT_ADMIN_ACCEPTED_ID.intValue()))
			.andExpect(jsonPath("$.tutorAccepted").value(DEFAULT_TUTOR_ACCEPTED.booleanValue()))
			.andExpect(jsonPath("$.tutorAcceptedId").value(DEFAULT_TUTOR_ACCEPTED_ID))
			.andExpect(jsonPath("$.tutorRejectedCount").value(DEFAULT_TUTOR_REJECTED_COUNT))
			.andExpect(jsonPath("$.cancelled").value(DEFAULT_CANCELLED.booleanValue()))
			.andExpect(jsonPath("$.requestTimes").value(DEFAULT_REQUEST_TIMES.toString()))
			.andExpect(jsonPath("$.readByAdmin").value(DEFAULT_READ_BY_ADMIN.booleanValue()));
	}

	@Test
	@Transactional
	public void getNonExistingBooking() throws Exception {
		// Get the booking
		restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateBookingCancelledByTutor() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings/updateBookingCancelledByTutor").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void createBookingAdminNotification() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void updateBookingAcceptedByTutor() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings/updateBookingAcceptedByTutor").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void updateBookingAssignedToTutor() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings/updateBookingAssignTutor").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void updateBookingRejectedByTutor() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings/updateBookingRejectedByTutor").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void updateBookingRequestRejectedByAdmin() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	@Test
	@Transactional
	public void updateBooking() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

		// Update the booking
		Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
		// Disconnect from session so that the updates on updatedBooking are not
		// directly saved in db
		em.detach(updatedBooking);
		updatedBooking
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.cancelled(UPDATED_CANCELLED)
			.requestTimes(UPDATED_REQUEST_TIMES)
			.readByAdmin(UPDATED_READ_BY_ADMIN);
		BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

		restBookingMockMvc
			.perform(put("/api/bookings").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
			.andExpect(status().isOk());

		// Validate the Booking in the database
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
		Booking testBooking = bookingList.get(bookingList.size() - 1);
		assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
		assertThat(testBooking.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
		assertThat(testBooking.getStartTime()).isEqualTo(UPDATED_START_TIME);
		assertThat(testBooking.getEndTime()).isEqualTo(UPDATED_END_TIME);
		assertThat(testBooking.getUserComments()).isEqualTo(UPDATED_USER_COMMENTS);
		assertThat(testBooking.getImportanceLevel()).isEqualTo(UPDATED_IMPORTANCE_LEVEL);
		assertThat(testBooking.getAdminAcceptedId()).isEqualTo(UPDATED_ADMIN_ACCEPTED_ID);
		assertThat(testBooking.isTutorAccepted()).isEqualTo(UPDATED_TUTOR_ACCEPTED);
		assertThat(testBooking.getTutorAcceptedId()).isEqualTo(UPDATED_TUTOR_ACCEPTED_ID);
		assertThat(testBooking.getTutorRejectedCount()).isEqualTo(UPDATED_TUTOR_REJECTED_COUNT);
		assertThat(testBooking.isCancelled()).isEqualTo(UPDATED_CANCELLED);
		assertThat(testBooking.getRequestTimes()).isEqualTo(UPDATED_REQUEST_TIMES);
		assertThat(testBooking.isReadByAdmin()).isEqualTo(UPDATED_READ_BY_ADMIN);
	}

	/*
	 * Check that search for bookings between times where there is bookings present will return a result
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsInTimeFrame() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// Get all the bookingList
		restBookingMockMvc
			.perform(get("/api/bookings?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())));
	}

	/*
	 * Check that search for user's bookings between times where there is bookings present will return no result
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getUserBookingsNoneInTimeFrame() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the booking database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours before
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().minus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect none to be returned
		restBookingMockMvc
			.perform(get("/api/bookings?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for user's bookings between times where there is bookings present will return a result
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getUserBookingsInTimeFrame() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 1 to be returned
		restBookingMockMvc
			.perform(get("/api/bookings?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())));
	}

	/*
	 * Check for users bookings within a time range, user id is non existent.
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getUserBookingsUserDoesNotExist() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// Set userId to a non-existent ID
		Long userId = userInfo.getId() + 1;

		// Get bookings for user in between the passed times, none expect to be returned
		restBookingMockMvc
			.perform(get("/api/bookings?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings between times where there is none present will return 0 results
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsNoneInTimeFrame() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours before
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().minus(12, ChronoUnit.HOURS).toEpochMilli();

		// Get all the bookingList
		restBookingMockMvc
			.perform(get("/api/bookings?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings with eagerload and user info works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest1() throws Exception {

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsDetails?eagerload=true&userInfo=true"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings with user info works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest2() throws Exception {

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);
		bookingRepository.save(booking);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsDetails?userInfo=true"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings between two dates works with user information works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest3() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);
		bookingRepository.save(booking2);
		// Initialize the database
		bookingRepository.save(booking);
		bookingRepository.flush();

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();
		
		// Get bookings for user in between the passed times, expect 2 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsDetails?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings between two dates before all booking times works, returns 0 bookings
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest4() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		// Initialize the database
		bookingRepository.save(booking);
		bookingRepository.flush();

		// Set start time 16 hours before start booking time and end time 12 hours before
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().minus(12, ChronoUnit.HOURS).toEpochMilli();

		// Get bookings for user in between the passed times, expect 0 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsDetails?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings between two dates after all booking times works, returns 0 bookings
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest5() throws Exception {

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		// Set start time 16 hours after start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().plus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		restBookingMockMvc
			.perform(get("/api/bookingsDetails?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings between two dates for one user works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest6() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 1 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsDetails?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userId=" + userId + "&userInfo=true&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings between two dates before any bookings for a particular user with no user info works
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest7() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours before
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().minus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 0 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsDetails?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings for a particular user with no user info works, returns 0 results
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest8() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours after start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().plus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 0 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsDetails?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings for a particular user with no user info works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest9() throws Exception {

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsDetails?userInfo=false&userId=8"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));
	}

	/*
	 * Check that search for bookings for a particular user with user info works
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsTest10() throws Exception {

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsDetails?userInfo=true&userId=8"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings with eagerload and user info works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getConfirmedBookings1() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?eagerload=true&userInfo=true"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings with user info works
	 * Necessary for 100 statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getConfirmedBookings2() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?userInfo=true"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings between two dates works with user information works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getConfirmedBookings3() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		// Initialize the database
		bookingRepository.save(booking);
		bookingRepository.flush();

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();
		
		// Get bookings for user in between the passed times, expect 2 to be
		// returned
		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings between two dates before all booking times works, returns 0 bookings
	 */
	@Test
	@Transactional
	public void getConfirmedBookings4() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		// Initialize the database
		bookingRepository.save(booking);
		bookingRepository.flush();

		// Set start time 16 hours before start booking time and end time 12 hours before
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().minus(12, ChronoUnit.HOURS).toEpochMilli();

		// Get bookings for user in between the passed times, expect 0 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings between two dates after all booking times works, returns 0 bookings
	 */
	@Test
	@Transactional
	public void getConfirmedBookings5() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		// Set start time 16 hours after start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().plus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings between two dates for one user works Necessary
	 * for 100% statement coverage Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getConfirmedBookings6() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 1 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userId=" + userId + "&userInfo=true&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Check that search for bookings between two dates before any bookings for a particular user with no user info works
	 */
	@Test
	@Transactional
	public void getConfirmedBookings7() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours before start booking time and end time 12 hours before
		long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().minus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 0 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings for a particular user with no user info works, returns 0 results
	 */
	@Test
	@Transactional
	public void getConfirmedBookings8() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		// Set start time 16 hours after start booking time and end time 12 hours after
		long startTimeMs = booking.getStartTime().plus(16, ChronoUnit.HOURS).toEpochMilli();
		long endTimeMs = booking.getStartTime().plus(12, ChronoUnit.HOURS).toEpochMilli();

		// setID to the userInfo id present in the booking
		Long userId = userInfo.getId();

		// Get bookings for user in between the passed times, expect 0 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?startTimeMs=" + startTimeMs + "&endTimeMs=" + endTimeMs + "&userInfo=true&userId=" + userId + "&sort=id,desc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Check that search for bookings for a particular user with no user info works
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getConfirmedBookings9() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?userInfo=false&userId=8"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));
	}

	/*
	 * Check that search for bookings for a particular user with user info works
	 */
	@Test
	@Transactional
	public void getConfirmedBookings10() throws Exception {

		// Set tutor accepted to true
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Add a second booking
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		bookingRepository.flush();

		restBookingMockMvc
			.perform(get("/api/bookingsConfirmed?userInfo=true&userId=8"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());
	}

	/*
	 * Start time set before result modified times
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsChangesTest1() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time 16 hours before modified timestamp
		Long startTimeMs = booking.getStartTime().minus(16, ChronoUnit.HOURS).toEpochMilli();
		Long userId = (Long) null;
		boolean userInfo = false;

		// Get bookings for user in between the passed times, expect 2 to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestDetailsChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));
	}

	/*
	 * Start time set after repository modified times row
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsChangesTest2() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time 16 hours after modified timestamp
		Long startTimeMs = booking.getStartTime().plus(16, ChronoUnit.HOURS).toEpochMilli();
		Long userId = (Long) null;
		boolean userInfo = false;

		// Get bookings with modifiedTimestamp after inputed start time, expect 0 results to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestDetailsChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Start time 1 second before result modified times with user ID provided
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsChangesTest3() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time 1 second before modified timestamp
		Long startTimeMs = booking.getStartTime().minus(1, ChronoUnit.SECONDS).toEpochMilli();
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get bookings with modifiedTimestamp after inputed start time, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestDetailsChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));
	}

	/*
	 * Start time 1 second after result modified times
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsChangesTest4() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time 1 second after modified timestamp rows
		Long startTimeMs = booking.getStartTime().plus(1, ChronoUnit.SECONDS).toEpochMilli();
		Long userId = null;
		boolean userInfo = false;

		// Get bookings with modifiedTimestamp after inputed start time, expect 0 results to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestDetailsChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Start time is null
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsChangesTest5() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time 1 second before modified timestamp
		Long startTimeMs = null;
		Long userId = null;
		boolean userInfo = false;

		// Get bookings with modifiedTimestamp after inputed start time, expect 400 error to be thrown because no startTimeMs passed in
		restBookingMockMvc
			.perform(get("/api/bookingsLatestDetailsChanges?startTimeMs="
					+ ((startTimeMs == null) ? "" : startTimeMs)
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter startTimeMs is missing")));
	}

	/*
	 * Start time is null and user ID is set
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getAllBookingsDetailsChangesTest6() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(DEFAULT_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to null
		Long startTimeMs = null;
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get bookings with modifiedTimestamp after inputed start time, expect 400 error to be thrown because no startTimeMs passed in
		restBookingMockMvc
			.perform(get("/api/bookingsLatestDetailsChanges?startTimeMs="
					+ ((startTimeMs == null) ? "" : startTimeMs)
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter startTimeMs is missing")));
	}

	/*
	 * Start time is set to 0
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges1() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to null
		Long startTimeMs = 0L;
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = true;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[0].booking.userInfos").isArray());
	}

	/*
	 * Start time is set to minus 16 hours Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges2() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));
	}

	/*
	 * Start time is set to null
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges3() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to null
		Long startTimeMs = null;
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect error due to startTimeMs not passed
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ ((startTimeMs == null) ? "" : startTimeMs)
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter startTimeMs or userId is missing")));

	}

	/*
	 * Start time is set to minus 16 hours user ID is set to null
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges4() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to tutor accepted ID
		Long userId = null;
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect error due to userId not passed
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ ((startTimeMs == null) ? "" : startTimeMs)
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter startTimeMs or userId is missing")));

	}

	/*
	 * Start time is set to minus 16 hours
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges5() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Set subject to null
		booking.setSubject(null);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned and subject to be null
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()))
			.andExpect(jsonPath("$.[0].subject", nullValue()));

	}

	/*
	 * Start time is set to plus 16 hours
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges6() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours more than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().plus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 0 results to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}

	/*
	 * Start time is set to minus 1 second
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges7() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 1 second less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(1, ChronoUnit.SECONDS).toEpochMilli();
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(DEFAULT_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Start time is set to plus 1 second
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges8() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 1 second more than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().plus(1, ChronoUnit.SECONDS).toEpochMilli();
		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 0 results to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}

	/*
	 * Start time set to less than 16 hours of bookings modified time & return tutor accepted bookings
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorPendingBookingsDetailsChanges9() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking2.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to tutor accepted ID
		Integer userId = booking2.getTutorAcceptedId();
		boolean pending = false;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsLatestTutorChanges?startTimeMs="
					+ startTimeMs
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)
					+ "&pending="
					+ pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(booking2.getAdminAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(booking2.getTutorRejectedCount().intValue()))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Start time is set to 0
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges1() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to null
		Long startTimeMs = 0L;
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = true;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[0].booking.userInfos").isArray());
	}

	/*
	 * Start time is set to minus 16 hours Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges2() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));
	}

	/*
	 * Start time is set to null
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges3() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to null
		Long startTimeMs = null;
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect error due to startTimeMs not passed
		restBookingMockMvc
			.perform(get("/api/bookingsLatestConfirmedChanges?startTimeMs="
					+ ((startTimeMs == null) ? "" : startTimeMs)
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter startTimeMs is missing")));

	}

	/*
	 * Start time is set to minus 16 hours user ID is set to null
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges4() throws Exception {

		// Set booking 1 to confirmed
		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to user's ID
		Long userId = null;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect error due to userId not passed
		restBookingMockMvc
			.perform(get("/api/bookingsLatestConfirmedChanges?startTimeMs="
					+ ((startTimeMs == null) ? "" : startTimeMs)
					+ "&userId="
					+ ((userId == null) ? "" : userId)
					+ "&userInfo="
					+ String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Start time is set to minus 16 hours
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges5() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set subject to null
		booking.setSubject(null);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned and subject to be null
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()))
			.andExpect(jsonPath("$.[0].subject", nullValue()));

	}

	/*
	 * Start time is set to plus 16 hours
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges6() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours more than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().plus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 0 results to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}

	/*
	 * Start time is set to minus 1 second
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges7() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 1 second less than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(1, ChronoUnit.SECONDS).toEpochMilli();
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(DEFAULT_TITLE.toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(DEFAULT_START_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(DEFAULT_END_TIME.toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(DEFAULT_USER_COMMENTS.toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(DEFAULT_IMPORTANCE_LEVEL.toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(DEFAULT_ADMIN_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(UPDATED_TUTOR_ACCEPTED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(DEFAULT_TUTOR_ACCEPTED_ID.intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(DEFAULT_MODIFIED_TIMESTAMP.toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(DEFAULT_TUTOR_REJECTED_COUNT)))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(DEFAULT_REQUEST_TIMES.toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(DEFAULT_READ_BY_ADMIN.booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Start time is set to plus 1 second
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges8() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking is accepted, should be returned
		Booking booking2 = new Booking()
			.title(DEFAULT_TITLE)
			.requestedBy(DEFAULT_REQUESTED_BY)
			.startTime(DEFAULT_START_TIME)
			.endTime(DEFAULT_END_TIME)
			.userComments(DEFAULT_USER_COMMENTS)
			.importanceLevel(DEFAULT_IMPORTANCE_LEVEL)
			.adminAcceptedId(DEFAULT_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(DEFAULT_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(DEFAULT_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(DEFAULT_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_CANCELLED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 16 hours more than the bookings
		Long startTimeMs = booking.getModifiedTimestamp().plus(1, ChronoUnit.SECONDS).toEpochMilli();
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 0 results to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}

	/*
	 * Start time set to less than 16 hours of bookings modified time & return tutor accepted bookings
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges9() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(true)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to 1 second less than the bookings
		Long startTimeMs = booking2.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		// Set userId to user's ID
		Long userId = userInfo.getId();
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(booking2.getAdminAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(booking2.getTutorRejectedCount().intValue()))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Start time set to less than 16 hours of bookings modified time & return tutor accepted bookings
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestConfirmedChanges10() throws Exception {

		booking.setTutorAccepted(UPDATED_TUTOR_ACCEPTED);
		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true, should not be returned
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time to same modified time of the first bookings, the second booking is a later date and will be returned also
		Long startTimeMs = booking.getModifiedTimestamp().toEpochMilli();
		// Set userId to user's ID
		Long userId = null;
		boolean userInfo = false;

		// Get pending tutor approval bookings with modifiedTimestamp after inputed
		// start time for a tutor, expect 1 result to be returned
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestConfirmedChanges?startTimeMs=" + startTimeMs + "&userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(booking2.getAdminAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Return confirmed accepted bookings Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorBookings1() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to pending tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(false)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set userId to tutor accepted ID
		Long userId = null;
		boolean pending = false;
		boolean userInfo = false;

		// Get accepted by tutor bookings, expect 400 error due to no userId parameter missing
		restBookingMockMvc
			.perform(get("/api/bookingsTutors?userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo) + "&pending=" + pending))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter userId is missing")));

	}

	/*
	 * Return confirmed booking assigned to that tutor
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorBookings2() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to pending tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(false)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = false;
		boolean userInfo = false;

		// Get accepted by tutor bookings, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsTutors?userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo) + "&pending=" + pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(booking.getAdminAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(booking.getTutorRejectedCount().intValue()))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[0].booking.bookingUserDetailsDTO", nullValue()))
			.andExpect(jsonPath("$.[0].booking.userInfos", nullValue()));

	}

	/*
	 * Return no results as ID does not exist in repository
	 */
	@Test
	@Transactional
	public void getTutorBookings3() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to accepted by tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to false
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(false)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set userId to tutor accepted ID
		Long userId = 100L;
		boolean pending = false;
		boolean userInfo = false;

		// Get accepted by tutor bookings, expect 0 results to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsTutors?userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo) + "&pending=" + pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}

	/*
	 * Return no results as ID does not exist in repository
	 */
	@Test
	@Transactional
	public void getTutorBookings4() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to pending tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to false
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(false)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set userId to tutor accepted ID
		Integer userId = booking2.getTutorAcceptedId();
		boolean pending = false;
		boolean userInfo = false;

		// Get accepted by tutor bookings, expect 0 results to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsTutors?userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo) + "&pending=" + pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}

	/*
	 * Return pending booking assigned to that tutor
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getTutorBookings5() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to pending tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(false)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set userId to tutor accepted ID
		Integer userId = booking2.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor bookings, expect 1 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsTutors?userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo) + "&pending=" + pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(booking2.getAdminAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(booking2.getTutorRejectedCount().intValue()))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").isArray())
			.andExpect(jsonPath("$.[*].booking.userInfos").isArray());

	}

	/*
	 * Return 0 results for tutor with no pending bookings
	 */
	@Test
	@Transactional
	public void getTutorBookings6() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to accepted tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has tutor accepted set to true
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(UPDATED_ADMIN_ACCEPTED_ID)
			.tutorAccepted(false)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set userId to tutor accepted ID
		Integer userId = booking.getTutorAcceptedId();
		boolean pending = true;
		boolean userInfo = false;

		// Get pending tutor bookings, expect 0 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsTutors?userId=" + ((userId == null) ? "" : userId) + "&userInfo=" + String.valueOf(userInfo) + "&pending=" + pending))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}

	/*
	 * 3 rows in database, return 2 results that are pending admin approval
	 */
	@Test
	@Transactional
	public void getBookingsPendingAdminApproval1() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to pending tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking does not have admin accepted
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		// Third booking does not have admin accepted
		Booking booking3 = new Booking()
				.title(UPDATED_TITLE)
				.requestedBy(UPDATED_REQUESTED_BY)
				.startTime(UPDATED_START_TIME)
				.endTime(UPDATED_END_TIME)
				.userComments(UPDATED_USER_COMMENTS)
				.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
				.adminAcceptedId(null)
				.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
				.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
				.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
				.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
				.cancelled(false)
				.requestTimes(DEFAULT_REQUEST_TIMES)
				.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking3);

		bookingRepository.flush();

		boolean userInfo = false;

		// Get accepted by tutor bookings, expect 2 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsPendingApproval?userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking2.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.userInfos").value(hasItem(nullValue())));
	}
	
	/*
	 * 3 rows in database, return 2 results that are pending admin approval
	 */
	@Test
	@Transactional
	public void getBookingsPendingAdminApproval2() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Set booking to pending tutor
		booking.setTutorAccepted(true);
		booking.setCancelled(false);

		// Set userInfos in booking
		booking.setUserInfos(userInfos);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking does not have admin accepted
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(false)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking2);

		// Third booking does not have admin accepted
		Booking booking3 = new Booking()
				.title(UPDATED_TITLE)
				.requestedBy(UPDATED_REQUESTED_BY)
				.startTime(UPDATED_START_TIME)
				.endTime(UPDATED_END_TIME)
				.userComments(UPDATED_USER_COMMENTS)
				.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
				.adminAcceptedId(null)
				.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
				.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
				.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
				.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
				.cancelled(false)
				.requestTimes(DEFAULT_REQUEST_TIMES)
				.readByAdmin(DEFAULT_READ_BY_ADMIN);

		bookingRepository.save(booking3);

		bookingRepository.flush();

		boolean userInfo = true;

		// Get accepted by tutor bookings, expect 2 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsPendingApproval?userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking2.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").value(hasItem(new ArrayList<>())))
			.andExpect(jsonPath("$.[*].booking.userInfos").value(hasItem(new ArrayList<>())));
	}
	
	/*
	 * 0 rows in database, return 0 results
	 */
	@Test
	@Transactional
	public void getBookingsPendingAdminApproval3() throws Exception {

		boolean userInfo = true;

		// Get accepted by tutor bookings, expect 2 result to be returned
		restBookingMockMvc
			.perform(get("/api/bookingsPendingApproval?userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));
	}
	
	/*
	 * Start time set to less than 16 hours of bookings modified time & return admin pending bookings
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges1() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to 16 hours less than bookings
		Long startTimeMs = booking.getModifiedTimestamp().minus(16, ChronoUnit.HOURS).toEpochMilli();
		boolean userInfo = false;

		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + startTimeMs + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking2.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.userInfos").value(hasItem(nullValue())));

	}
	
	/*
	 * Start time set to more than 16 hours of bookings modified time & return no bookings
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges2() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to 16 hours more than bookings
		Long startTimeMs = booking2.getModifiedTimestamp().plus(16, ChronoUnit.HOURS).toEpochMilli();
		boolean userInfo = false;
		
		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + startTimeMs + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}
	
	/*
	 * Start time set to less than 16 hours of bookings modified time & return admin pending bookings
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges3() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to 0 seconds
		Long startTimeMs = 0L;
		boolean userInfo = false;

		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + startTimeMs + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking2.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.userInfos").value(hasItem(nullValue())));
		

	}
	
	/*
	 * Start time set to 0 & return admin pending bookings
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges4() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to 0 seconds
		Long startTimeMs = booking.getModifiedTimestamp().minus(1, ChronoUnit.SECONDS).toEpochMilli();
		boolean userInfo = false;

		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + startTimeMs + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking2.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.requestTimes").value(hasItem(booking2.getRequestTimes().toString())))
			.andExpect(jsonPath("$.[*].booking.readByAdmin").value(hasItem(booking2.isReadByAdmin().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.userInfos").value(hasItem(nullValue())));

	}
	
	/*
	 * Start time set to 0 & return admin pending bookings
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges5() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to 0 seconds
		Long startTimeMs = booking.getModifiedTimestamp().toEpochMilli();
		boolean userInfo = false;

		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + startTimeMs + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(1))
			.andExpect(jsonPath("$.[*].booking.id").value(hasItem(booking2.getId().intValue())))
			.andExpect(jsonPath("$.[*].booking.title").value(hasItem(booking2.getTitle().toString())))
			.andExpect(jsonPath("$.[*].booking.requestedBy").value(hasItem(booking2.getRequestedBy().toString())))
			.andExpect(jsonPath("$.[*].booking.startTime").value(hasItem(booking2.getStartTime().toString())))
			.andExpect(jsonPath("$.[*].booking.endTime").value(hasItem(booking2.getEndTime().toString())))
			.andExpect(jsonPath("$.[*].booking.userComments").value(hasItem(booking2.getUserComments().toString())))
			.andExpect(jsonPath("$.[*].booking.importanceLevel").value(hasItem(booking2.getImportanceLevel().toString())))
			.andExpect(jsonPath("$.[*].booking.adminAcceptedId").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAccepted").value(hasItem(booking2.isTutorAccepted().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.tutorAcceptedId").value(hasItem(booking2.getTutorAcceptedId().intValue())))
			.andExpect(jsonPath("$.[*].booking.modifiedTimestamp").value(hasItem(booking2.getModifiedTimestamp().toString())))
			.andExpect(jsonPath("$.[*].booking.tutorRejectedCount").value(hasItem(booking2.getTutorRejectedCount().intValue())))
			.andExpect(jsonPath("$.[*].booking.cancelled").value(hasItem(booking2.isCancelled().booleanValue())))
			.andExpect(jsonPath("$.[*].booking.bookingUserDetailsDTO").value(hasItem(nullValue())))
			.andExpect(jsonPath("$.[*].booking.userInfos").value(hasItem(nullValue())));

	}
	
	/*
	 * Start time set to more than 1 second of bookings modified time & return 0 results
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges6() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to plus 1 second of bookings
		Long startTimeMs = booking2.getModifiedTimestamp().plus(1, ChronoUnit.SECONDS).toEpochMilli();
		boolean userInfo = false;

		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + startTimeMs + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.size()").value(0));

	}
	
	/*
	 * Start time set to more than 1 second of bookings modified time & return 0 results
	 */
	@Test
	@Transactional
	public void getBookingsLatestPendingApprovalChanges7() throws Exception {

		// Initialize userInfo database
		userInfoRepository.saveAndFlush(userInfo);

		// Create userInfos for booking
		Set<UserInfo> userInfos = new HashSet<UserInfo>();
		userInfos.add(userInfo);

		// Initialize the database
		bookingRepository.save(booking);

		// Second booking has been set to pending admin approval
		Booking booking2 = new Booking()
			.title(UPDATED_TITLE)
			.requestedBy(UPDATED_REQUESTED_BY)
			.startTime(UPDATED_START_TIME)
			.endTime(UPDATED_END_TIME)
			.userComments(UPDATED_USER_COMMENTS)
			.importanceLevel(UPDATED_IMPORTANCE_LEVEL)
			.adminAcceptedId(null)
			.tutorAccepted(UPDATED_TUTOR_ACCEPTED)
			.tutorAcceptedId(UPDATED_TUTOR_ACCEPTED_ID)
			.tutorRejectedCount(UPDATED_TUTOR_REJECTED_COUNT)
			.modifiedTimestamp(UPDATED_MODIFIED_TIMESTAMP)
			.cancelled(DEFAULT_TUTOR_ACCEPTED)
			.requestTimes(DEFAULT_REQUEST_TIMES)
			.readByAdmin(DEFAULT_READ_BY_ADMIN);

		// Set userInfos in booking
		booking2.setUserInfos(userInfos);

		bookingRepository.save(booking2);
		bookingRepository.flush();

		// Set start time set to null
		Long startTimeMs = null;
		boolean userInfo = false;

		restBookingMockMvc
			.perform(
				get("/api/bookingsLatestPendingApprovalChanges?startTimeMs=" + ((startTimeMs == null) ? "" : startTimeMs) + "&userInfo=" + String.valueOf(userInfo)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title", is("Parameter startTimeMs is missing")));

	}
	
	@Test
	@Transactional
	public void deleteBooking() throws Exception {
		// Initialize the database
		bookingRepository.saveAndFlush(booking);

		int databaseSizeBeforeDelete = bookingRepository.findAll().size();

		// Get the booking
		restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate the database is empty
		List<Booking> bookingList = bookingRepository.findAll();
		assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
	}
	
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Booking.class);
		Booking booking1 = new Booking();
		booking1.setId(1L);
		Booking booking2 = new Booking();
		booking2.setId(booking1.getId());
		assertThat(booking1).isEqualTo(booking2);
		booking2.setId(2L);
		assertThat(booking1).isNotEqualTo(booking2);
		booking1.setId(null);
		assertThat(booking1).isNotEqualTo(booking2);
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(BookingDTO.class);
		BookingDTO bookingDTO1 = new BookingDTO();
		bookingDTO1.setId(1L);
		BookingDTO bookingDTO2 = new BookingDTO();
		assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
		bookingDTO2.setId(bookingDTO1.getId());
		assertThat(bookingDTO1).isEqualTo(bookingDTO2);
		bookingDTO2.setId(2L);
		assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
		bookingDTO1.setId(null);
		assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
	}

	@Test
	@Transactional
	public void testEntityFromId() {
		assertThat(bookingMapper.fromId(42L).getId()).isEqualTo(42);
		assertThat(bookingMapper.fromId(null)).isNull();
	}
}
