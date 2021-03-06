package com.itlc.thelearningzone.web.rest;

import com.itlc.thelearningzone.ThelearningzoneApp;
import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.domain.BookingUserDetails;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.domain.UserInfo;
import com.itlc.thelearningzone.repository.BookingRepository;
import com.itlc.thelearningzone.repository.BookingUserDetailsRepository;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.service.BookingUserDetailsService;
import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;
import com.itlc.thelearningzone.service.mapper.BookingUserDetailsMapper;
import com.itlc.thelearningzone.web.rest.errors.ExceptionTranslator;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;


import static com.itlc.thelearningzone.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;
/**
 * Test class for the BookingUserDetailsResource REST controller.
 *
 * @see BookingUserDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class BookingUserDetailsResourceIntTest {

    private static final String DEFAULT_USER_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_USER_FEEDBACK = "BBBBBBBBBB";

    private static final OrdinalScale DEFAULT_USER_SATISFACTION = OrdinalScale.NONE;
    private static final OrdinalScale UPDATED_USER_SATISFACTION = OrdinalScale.LOW;

    private static final Instant DEFAULT_USERCHECK_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_USERCHECK_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_USERCHECK_OUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_USERCHECK_OUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_USER_CANCELLED = false;
    private static final Boolean UPDATED_USER_CANCELLED = true;

    private static final Boolean DEFAULT_TUTOR_REJECTED = false;
    private static final Boolean UPDATED_TUTOR_REJECTED = true;
    
    private static final String DEFAULT_LOGIN = "johndoe";
    
    private static final String DEFAULT_BOOKING_TITLE = "AAAAAAAAAA";

    @Autowired
    private BookingUserDetailsRepository bookingUserDetailsRepository;

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingUserDetailsMapper bookingUserDetailsMapper;

    @Autowired
    private BookingUserDetailsService bookingUserDetailsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookingUserDetailsMockMvc;

    private BookingUserDetails bookingUserDetails;
    
    private User user;
    
    private UserInfo userInfo;
    
    private Booking booking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookingUserDetailsResource bookingUserDetailsResource = new BookingUserDetailsResource(bookingUserDetailsService);
        this.restBookingUserDetailsMockMvc = MockMvcBuilders.standaloneSetup(bookingUserDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingUserDetails createEntity(EntityManager em) {
        BookingUserDetails bookingUserDetails = new BookingUserDetails()
            .userFeedback(DEFAULT_USER_FEEDBACK)
            .userSatisfaction(DEFAULT_USER_SATISFACTION)
            .usercheckInTime(DEFAULT_USERCHECK_IN_TIME)
            .usercheckOutTime(DEFAULT_USERCHECK_OUT_TIME)
            .userCancelled(DEFAULT_USER_CANCELLED)
            .tutorRejected(DEFAULT_TUTOR_REJECTED);
        return bookingUserDetails;
    }
    
    public static User createUserEntity(EntityManager em) {
		User user = new User();
		user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
		user.setPassword(RandomStringUtils.random(60));

		return user;
	}
    
    public static UserInfo createUserInfoEntity(EntityManager em) {
		UserInfo userInfo = new UserInfo();
		
		return userInfo;
	}
    
    public static Booking createBookingEntity(EntityManager em) {
		Booking booking = new Booking()
			.title(DEFAULT_BOOKING_TITLE + RandomStringUtils.randomAlphabetic(5))
			.importanceLevel(DEFAULT_USER_SATISFACTION)
			.requestedBy("test user")
			.startTime(Instant.ofEpochMilli(0L))
			.endTime(Instant.now().truncatedTo(ChronoUnit.MILLIS))
			.cancelled(false);
		
		return booking;
	}

    @Before
    public void initTest() {
    	user = createUserEntity(em);
    	userInfo = createUserInfoEntity(em);
    	booking = createBookingEntity(em);
        bookingUserDetails = createEntity(em);
    }
    
    /**
     * Check that an existing BookingUserDetails entity has been updated to cancelled
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void cancelAttendance1() throws Exception
	{
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/cancelAttendance/" + booking.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.isUserCancelled()).isEqualTo(UPDATED_USER_CANCELLED);
	}
    
    /**
     * Check that a non existent BookingUserDetails entity is caught in a BadRequestAlertException
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void cancelAttendance2() throws Exception
	{
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);
        
        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);
        
        bookingUserDetailsDTO.setId(null);
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/cancelAttendance/" + bookingUserDetailsDTO.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isBadRequest());
        
     // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.isUserCancelled()).isEqualTo(DEFAULT_USER_CANCELLED);

	}
    
    /**
     * Check that a null login String is caught in an IllegalArgumentException
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void cancelAttendance3() throws Exception
	{
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);
        
        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);
        
        user.setLogin(null);
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/cancelAttendance/" + booking.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().is5xxServerError());
        
     // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.isUserCancelled()).isEqualTo(DEFAULT_USER_CANCELLED);

	}
    
    /**
     * Check that an existing BookingUserDetails entity has been updated with a check-in time stamp
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void checkIn1() throws Exception
	{
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/checkIn/" + booking.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(!testBookingUserDetails.getUsercheckInTime().equals(null));
	}
    
    /**
     * Check that a non existent BookingUserDetails entity is caught in a BadRequestAlertException
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void checkIn2() throws Exception
	{
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        bookingUserDetailsDTO.setId(null);
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/checkIn/" + bookingUserDetailsDTO.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.getUsercheckInTime().equals(null));
	}
    
    /**
     * Check that a null login String is caught in an IllegalArgumentException
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void checkIn3() throws Exception
	{
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        user.setLogin(null);
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/checkIn/" + booking.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().is5xxServerError());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.getUsercheckInTime().equals(null));
	}

    /**
     * Check that an existing BookingUserDetails entity has been updated with a check-in time stamp
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void checkInBlackBox1() throws Exception
	{
    	Instant currentTime = Instant.now();
    	
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/checkIn/" + booking.getId() + "/" + user.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.getUsercheckInTime()).isAfter(currentTime);
	}
    
    /**
     * Check that the correct error response occurs when user passed in does not exist
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void checkInBlackBox2() throws Exception
	{
    	
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        String fakeUser = "user123";
        
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/checkIn/" + booking.getId() + "/" + fakeUser)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.detail").value("User login does not exist"));

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
	}
    
    /**
     * Check that the correct error response occurs when booking ID passed in does not exist
     * Necessary for 100% statement coverage
     * Necessary for 100% condition coverage
     */
    @Test
	@Transactional
	public void checkInBlackBox3() throws Exception
	{
    	
    	userInfo.setUser(user);
		booking.getUserInfos().add(userInfo);
		bookingUserDetails.setBooking(booking);
		bookingUserDetails.setUserInfo(userInfo);
		
		// Initialize the database
		userRepository.saveAndFlush(user);
		userInfoRepository.saveAndFlush(userInfo);
		bookingRepository.saveAndFlush(booking);
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the required entities
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBookingUserDetails);
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        Long fakeBookingId = 100l;
        
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details/checkIn/" + fakeBookingId + "/" + updatedUser.getLogin())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.detail").value("Booking with that ID does not exist"));

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
	}
    
    @Test
    @Transactional
    public void createBookingUserDetails() throws Exception {
        int databaseSizeBeforeCreate = bookingUserDetailsRepository.findAll().size();

        // Create the BookingUserDetails
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(bookingUserDetails);
        restBookingUserDetailsMockMvc.perform(post("/api/booking-user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.getUserFeedback()).isEqualTo(DEFAULT_USER_FEEDBACK);
        assertThat(testBookingUserDetails.getUserSatisfaction()).isEqualTo(DEFAULT_USER_SATISFACTION);
        assertThat(testBookingUserDetails.getUsercheckInTime()).isEqualTo(DEFAULT_USERCHECK_IN_TIME);
        assertThat(testBookingUserDetails.getUsercheckOutTime()).isEqualTo(DEFAULT_USERCHECK_OUT_TIME);
        assertThat(testBookingUserDetails.isUserCancelled()).isEqualTo(DEFAULT_USER_CANCELLED);
        assertThat(testBookingUserDetails.isTutorRejected()).isEqualTo(DEFAULT_TUTOR_REJECTED);
    }

    @Test
    @Transactional
    public void createBookingUserDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookingUserDetailsRepository.findAll().size();

        // Create the BookingUserDetails with an existing ID
        bookingUserDetails.setId(1L);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(bookingUserDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingUserDetailsMockMvc.perform(post("/api/booking-user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBookingUserDetails() throws Exception {
        // Initialize the database
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        // Get all the bookingUserDetailsList
        restBookingUserDetailsMockMvc.perform(get("/api/booking-user-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingUserDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].userFeedback").value(hasItem(DEFAULT_USER_FEEDBACK.toString())))
            .andExpect(jsonPath("$.[*].userSatisfaction").value(hasItem(DEFAULT_USER_SATISFACTION.toString())))
            .andExpect(jsonPath("$.[*].usercheckInTime").value(hasItem(DEFAULT_USERCHECK_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].usercheckOutTime").value(hasItem(DEFAULT_USERCHECK_OUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].userCancelled").value(hasItem(DEFAULT_USER_CANCELLED.booleanValue())))
            .andExpect(jsonPath("$.[*].tutorRejected").value(hasItem(DEFAULT_TUTOR_REJECTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBookingUserDetails() throws Exception {
        // Initialize the database
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        // Get the bookingUserDetails
        restBookingUserDetailsMockMvc.perform(get("/api/booking-user-details/{id}", bookingUserDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookingUserDetails.getId().intValue()))
            .andExpect(jsonPath("$.userFeedback").value(DEFAULT_USER_FEEDBACK.toString()))
            .andExpect(jsonPath("$.userSatisfaction").value(DEFAULT_USER_SATISFACTION.toString()))
            .andExpect(jsonPath("$.usercheckInTime").value(DEFAULT_USERCHECK_IN_TIME.toString()))
            .andExpect(jsonPath("$.usercheckOutTime").value(DEFAULT_USERCHECK_OUT_TIME.toString()))
            .andExpect(jsonPath("$.userCancelled").value(DEFAULT_USER_CANCELLED.booleanValue()))
            .andExpect(jsonPath("$.tutorRejected").value(DEFAULT_TUTOR_REJECTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBookingUserDetails() throws Exception {
        // Get the bookingUserDetails
        restBookingUserDetailsMockMvc.perform(get("/api/booking-user-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookingUserDetails() throws Exception {
        // Initialize the database
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Update the bookingUserDetails
        BookingUserDetails updatedBookingUserDetails = bookingUserDetailsRepository.findById(bookingUserDetails.getId()).get();
        // Disconnect from session so that the updates on updatedBookingUserDetails are not directly saved in db
        em.detach(updatedBookingUserDetails);
        updatedBookingUserDetails
            .userFeedback(UPDATED_USER_FEEDBACK)
            .userSatisfaction(UPDATED_USER_SATISFACTION)
            .usercheckInTime(UPDATED_USERCHECK_IN_TIME)
            .usercheckOutTime(UPDATED_USERCHECK_OUT_TIME)
            .userCancelled(UPDATED_USER_CANCELLED)
            .tutorRejected(UPDATED_TUTOR_REJECTED);
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(updatedBookingUserDetails);

        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookingUserDetails testBookingUserDetails = bookingUserDetailsList.get(bookingUserDetailsList.size() - 1);
        assertThat(testBookingUserDetails.getUserFeedback()).isEqualTo(UPDATED_USER_FEEDBACK);
        assertThat(testBookingUserDetails.getUserSatisfaction()).isEqualTo(UPDATED_USER_SATISFACTION);
        assertThat(testBookingUserDetails.getUsercheckInTime()).isEqualTo(UPDATED_USERCHECK_IN_TIME);
        assertThat(testBookingUserDetails.getUsercheckOutTime()).isEqualTo(UPDATED_USERCHECK_OUT_TIME);
        assertThat(testBookingUserDetails.isUserCancelled()).isEqualTo(UPDATED_USER_CANCELLED);
        assertThat(testBookingUserDetails.isTutorRejected()).isEqualTo(UPDATED_TUTOR_REJECTED);
    }

    @Test
    @Transactional
    public void updateNonExistingBookingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookingUserDetailsRepository.findAll().size();

        // Create the BookingUserDetails
        BookingUserDetailsDTO bookingUserDetailsDTO = bookingUserDetailsMapper.toDto(bookingUserDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingUserDetailsMockMvc.perform(put("/api/booking-user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingUserDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingUserDetails in the database
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBookingUserDetails() throws Exception {
        // Initialize the database
        bookingUserDetailsRepository.saveAndFlush(bookingUserDetails);

        int databaseSizeBeforeDelete = bookingUserDetailsRepository.findAll().size();

        // Get the bookingUserDetails
        restBookingUserDetailsMockMvc.perform(delete("/api/booking-user-details/{id}", bookingUserDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BookingUserDetails> bookingUserDetailsList = bookingUserDetailsRepository.findAll();
        assertThat(bookingUserDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingUserDetails.class);
        BookingUserDetails bookingUserDetails1 = new BookingUserDetails();
        bookingUserDetails1.setId(1L);
        BookingUserDetails bookingUserDetails2 = new BookingUserDetails();
        bookingUserDetails2.setId(bookingUserDetails1.getId());
        assertThat(bookingUserDetails1).isEqualTo(bookingUserDetails2);
        bookingUserDetails2.setId(2L);
        assertThat(bookingUserDetails1).isNotEqualTo(bookingUserDetails2);
        bookingUserDetails1.setId(null);
        assertThat(bookingUserDetails1).isNotEqualTo(bookingUserDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingUserDetailsDTO.class);
        BookingUserDetailsDTO bookingUserDetailsDTO1 = new BookingUserDetailsDTO();
        bookingUserDetailsDTO1.setId(1L);
        BookingUserDetailsDTO bookingUserDetailsDTO2 = new BookingUserDetailsDTO();
        assertThat(bookingUserDetailsDTO1).isNotEqualTo(bookingUserDetailsDTO2);
        bookingUserDetailsDTO2.setId(bookingUserDetailsDTO1.getId());
        assertThat(bookingUserDetailsDTO1).isEqualTo(bookingUserDetailsDTO2);
        bookingUserDetailsDTO2.setId(2L);
        assertThat(bookingUserDetailsDTO1).isNotEqualTo(bookingUserDetailsDTO2);
        bookingUserDetailsDTO1.setId(null);
        assertThat(bookingUserDetailsDTO1).isNotEqualTo(bookingUserDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bookingUserDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bookingUserDetailsMapper.fromId(null)).isNull();
    }
}
