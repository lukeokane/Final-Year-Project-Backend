package com.itlc.thelearningzone.service;
import com.itlc.thelearningzone.config.Constants;

import com.itlc.thelearningzone.ThelearningzoneApp;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.domain.UserInfo;
import com.itlc.thelearningzone.domain.enumeration.OrdinalScale;
import com.itlc.thelearningzone.repository.BookingRepository;
import com.itlc.thelearningzone.repository.ResourceRepository;
import com.itlc.thelearningzone.repository.SubjectRepository;
import com.itlc.thelearningzone.repository.TopicRepository;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.domain.Booking;
import com.itlc.thelearningzone.domain.Resource;
import com.itlc.thelearningzone.domain.Subject;
import com.itlc.thelearningzone.domain.Topic;

import io.github.jhipster.config.JHipsterProperties;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class MailServiceIntTest {

    private static final OrdinalScale DEFAULT_USER_SATISFACTION = OrdinalScale.NONE;

    @Autowired
    private BookingRepository bookingRepository;
    
	@Mock
	private BookingRepository bookingRepositoryMock;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> messageCaptor;
    
    @Autowired
    private EntityManager em;

    private MailService mailService;
    
    private User user;
    
    private UserInfo userInfo;
    
    private User tutorUser;
    
    private UserInfo tutorUserInfo;
    
    private Booking booking;
    
    private Booking bookingEdited;
    
    private Subject subject;
    
    private Topic topic;
    
    private Resource resource1;
    
    private Resource resource2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        mailService = new MailService(jHipsterProperties, javaMailSender, messageSource, templateEngine);
    }
    
    
    /**
     * --------------- ENTITIES ---------------
     * Create entities for this test.
     *
     * These are static methods, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUserEntity(EntityManager em) {
		User user = new User();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail(RandomStringUtils.randomAlphabetic(5) + "D00187490@student.dkit.ie");
		user.setLogin(RandomStringUtils.randomAlphabetic(5) + "D00187490");
		user.setPassword(RandomStringUtils.random(60));
		user.setLangKey("en");

		return user;
	}
    
    public static User createUserEntity2(EntityManager em) {
		User user = new User();
		user.setFirstName("Jane");
		user.setLastName("Doe");
		user.setEmail(RandomStringUtils.randomAlphabetic(5) + "janedoe@student.dkit.ie");
		user.setLogin(RandomStringUtils.randomAlphabetic(5) + "jane.doe");
		user.setPassword(RandomStringUtils.random(60));
		user.setLangKey("en");

		return user;
	}
    
    public static UserInfo createUserInfoEntity(EntityManager em) {
		UserInfo userInfo = new UserInfo();
		return userInfo;
	}
    
    public static UserInfo createUserInfoEntity2(EntityManager em) {
		UserInfo userInfo = new UserInfo();

		return userInfo;
	}
    
    public static Booking createBookingEntity(EntityManager em) {
		Booking booking = new Booking()
			.title("test booking")
			.importanceLevel(DEFAULT_USER_SATISFACTION)
			.requestedBy("test user")
			.startTime(Instant.ofEpochMilli(0L))
			.endTime(Instant.now().truncatedTo(ChronoUnit.MILLIS))
			.cancelled(false);
		
		return booking;
	}
    
    public static Subject createSubjectEntity(EntityManager em) {
    	Subject subject = new Subject()
    		.title("Subject Title")
    		.subjectCode("ABC123");
    
    	return subject;
    }
    
    public static Topic createTopicEntity(EntityManager em) {
    	Topic topic = new Topic()
    		.title("Topic Title");
    	
    	return topic;
    }
    
    public static Resource createResourceEntity(EntityManager em) {
    	Resource resource = new Resource()
    		.title("Resource Title")
        	.resourceURL("www.dkit.ie");
    	
    	return resource;
    }
    
    @Before
    public void initTest() {
    	user = createUserEntity(em);
    	userInfo = createUserInfoEntity(em);
    	
    	tutorUser = createUserEntity2(em);    	
    	tutorUserInfo = createUserInfoEntity2(em);

    	subject = createSubjectEntity(em);
    	topic = createTopicEntity(em);
    	resource1 = createResourceEntity(em);
    	booking = createBookingEntity(em);
    	bookingEdited = createBookingEntity(em);
    	
    	booking.setSubject(subject);
    	bookingEdited.setSubject(subject);
    	
    	Set<Topic> topics = new HashSet<Topic>();
    	topics.add(topic);
    	subject.setTopics(topics);
    	
    	userInfo.setUser(user);
    	tutorUserInfo.setUser(tutorUser);
    	
    	resource1.setTitle("Topic 1 Title");
    	resource2 = createResourceEntity(em);
    	resource2.setTitle("Topic 2 Title");
    	
    	resource1.setTopic(topic);
    	resource2.setTopic(topic);
    	
    }
    /**
     * --------------- END ENTITIES ---------------
     */

    @Test
    public void testSendEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    public void testSendHtmlEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, true);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendMultipartEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, false);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos.toString()).isEqualTo("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    public void testSendMultipartHtmlEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, true);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos.toString()).isEqualTo("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendEmailFromTemplate() throws Exception {
        User user = new User();
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        user.setLangKey("en");
        mailService.sendEmailFromTemplate(user, "mail/testEmail", "email.test.title");
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("test title");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isEqualToNormalizingNewlines("<html>test title, http://127.0.0.1:8080, john</html>\n");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendActivationEmail() throws Exception {
        User user = new User();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        mailService.sendActivationEmail(user);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testCreationEmail() throws Exception {
        User user = new User();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        mailService.sendCreationEmail(user);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendPasswordResetMail() throws Exception {
        User user = new User();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        mailService.sendPasswordResetMail(user);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }
    
    @Test
    public void testSendCancellationEmail() throws Exception {
        User user = new User();
        User tutorUser = new User();
        Booking booking = new Booking();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        booking.setRequestedBy("John Doe");
        booking.setStartTime(Instant.parse("2014-11-12T05:50:00.0Z"));
        booking.setTitle("Java");
        mailService.sendBookingCancelledEmail(booking, user, tutorUser);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
       
      
    }
     
	/*
	 * Check that rejected mail sends and includes topic and resources titles of the booking
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    public void testSendBookingRejectedEmail() throws Exception {
    	
    	/* Create user */
    	User bookingUser = new User();
    	bookingUser.setFirstName("John");
    	bookingUser.setLastName("Doe");
    	bookingUser.setEmail("lukecjokane@gmail.com");
    	bookingUser.setLangKey("en");
    	UserInfo bookingUserInfo = new UserInfo();
    	bookingUserInfo.setUser(bookingUser);
    	
    	/* Create booking */
        Booking booking = new Booking();
        booking.setRequestedBy(bookingUser.getFirstName() + " " + bookingUser.getLastName());
        booking.setTitle("Object Oriented Programming");
        
        /* Create topics under booking */
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setTitle("Topic 1 Title");
        
        /* Create resources under booking */
        Resource resource1 = new Resource();
        resource1.setId(1L);
        resource1.setTitle("Resource 1 Title");
        resource1.setResourceURL("www.example.com");
        resource1.setTopic(topic);
        
        Resource resource2 = new Resource();
        resource2.setId(2L);
        resource2.setTitle("Resource 2 Title");
        resource2.setResourceURL("www.dkit.ie");
        resource2.setTopic(topic);
     
        /* Add resources to a list */
        List<Resource> resources = new ArrayList<Resource>();
        resources.add(resource1);
        resources.add(resource2);
        
        /* Add user to a set */      
        Set<UserInfo> bookingUsers = new HashSet<UserInfo>();
        bookingUsers.add(bookingUserInfo);
        
        mailService.sendBookingNotPossibleEmail(booking, bookingUsers, resources);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(bookingUserInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title, the topic and it's resources are present in email */
        String emailBody = message.getContent().toString();
        
        assertThat(emailBody).contains("could not be accepted");  
        assertThat(emailBody).contains(booking.getTitle());  
        assertThat(emailBody).contains(topic.getTitle());
        assertThat(emailBody).contains(resource2.getTitle());        
    }
    
    /*
	 * Check that cancelled mail sends and includes topic and resources titles of the booking
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    public void testSendBookingCancelledEmail() throws Exception {
    	
    	/* Create user */
    	User bookingUser = new User();
    	bookingUser.setFirstName("John");
    	bookingUser.setLastName("Doe");
    	bookingUser.setEmail("lukecjokane@gmail.com");
    	bookingUser.setLangKey("en");
    	UserInfo bookingUserInfo = new UserInfo();
    	bookingUserInfo.setUser(bookingUser);
    	
    	/* Create booking */
        Booking booking = new Booking();
        booking.setRequestedBy(bookingUser.getFirstName() + " " + bookingUser.getLastName());
        booking.setTitle("Object Oriented Programming");
        booking.setAdminAcceptedId(1);
        
        /* Create topics under booking */
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setTitle("Topic 1 Title");
        
        /* Create resources under booking */
        Resource resource1 = new Resource();
        resource1.setId(1L);
        resource1.setTitle("Resource 1 Title");
        resource1.setResourceURL("www.example.com");
        resource1.setTopic(topic);
        
        Resource resource2 = new Resource();
        resource2.setId(2L);
        resource2.setTitle("Resource 2 Title");
        resource2.setResourceURL("www.dkit.ie");
        resource2.setTopic(topic);
     
        /* Add resources to a list */
        List<Resource> resources = new ArrayList<Resource>();
        resources.add(resource1);
        resources.add(resource2);
        
        /* Add user to a set */      
        Set<UserInfo> bookingUsers = new HashSet<UserInfo>();
        bookingUsers.add(bookingUserInfo);
        
        mailService.sendBookingNotPossibleEmail(booking, bookingUsers, resources);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(bookingUserInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title, the topic and it's resources are present in email */
        String emailBody = message.getContent().toString();
        
        assertThat(emailBody).contains("has had to be cancelled");  
        assertThat(emailBody).contains(booking.getTitle());  
        assertThat(emailBody).contains(topic.getTitle());
        assertThat(emailBody).contains(resource2.getTitle());        
    }
    
	/*
	 * Check that mail sends and does not include topic and resources titles since the booking has no subject ID
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    @Transactional
    public void testSendBookingConfirmedEmail() throws Exception {
    		
    	
		// Initialize the database
        userInfoRepository.save(userInfo);
        userInfoRepository.save(tutorUserInfo);
        userInfoRepository.flush();
        topicRepository.saveAndFlush(topic);
        resourceRepository.save(resource1);
        resourceRepository.save(resource2);
        resourceRepository.flush();
        subjectRepository.saveAndFlush(subject);
        
    	// Set booking to accepted and set tutor
    	Set<UserInfo> userInfos = new HashSet<UserInfo>();
    	userInfos.add(userInfo);
    	booking.setUserInfos(userInfos);
    	booking.setTutorAccepted(true);
    	booking.setTutorAcceptedId(tutorUserInfo.getId().intValue());

		bookingRepository.saveAndFlush(booking);

        // Get the required entities
        Booking updatedBooking = bookingRepository.findOneWithEagerRelationships(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        List<Resource> resources = resourceRepository.findAllResourcesInBooking(booking.getId());
        User tutor = userRepository.findById(updatedBooking.getTutorAcceptedId().longValue()).get();
        
        mailService.sendBookingConfirmedEmail(updatedBooking, updatedBooking.getUserInfos(), tutor, resources);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(userInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title, the topic and it's resources are present in email */
        String emailBody = message.getContent().toString();
        
        assertThat(emailBody).contains(booking.getTitle());
        //assertThat(emailBody).contains(topic.getTitle());
        //assertThat(emailBody).contains(resource1.getTitle());
        
        // Check if the tutor's name is present
        assertThat(emailBody).contains(tutor.getFirstName() + " " + tutor.getLastName());
        
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
        em.detach(tutor);
        for (int i = 0; i < resources.size(); i++)
        {
        em.detach(resources.get(i));      
        }
        
    }
    
	/*
	 * Check that booking successfully updates with no subject present
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    @Transactional
    public void testSendBookingConfirmedEmail2() throws Exception {  
    	
		// Initialize the database
        userInfoRepository.save(userInfo);
        userInfoRepository.save(tutorUserInfo);
        userInfoRepository.flush();
        topicRepository.saveAndFlush(topic);
        resourceRepository.save(resource1);
        resourceRepository.save(resource2);
        resourceRepository.flush();
        subjectRepository.saveAndFlush(subject);
        
    	// Set subject to null
    	booking.setSubject(null);
    	
    	// Add requesting user to user infos.
    	Set<UserInfo> userInfos = new HashSet<UserInfo>();
    	userInfos.add(userInfo);
    	
    	// Set booking to accepted and set tutor
    	booking.setUserInfos(userInfos);
        
		bookingRepository.saveAndFlush(booking);

        // Get the required entities
        Booking updatedBooking = bookingRepository.findOneWithEagerRelationships(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Resources is empty since booking has no subject
        List<Resource> resources = new ArrayList<>();
        // Tutor is null since it is not a tutorial
        User tutor = null;

        mailService.sendBookingConfirmedEmail(updatedBooking, updatedBooking.getUserInfos(), tutor, resources);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(userInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title present in email */
        String emailBody = message.getContent().toString();
        
        /* Check email includes strings used for non-tutorial bookings*/

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                .withZone(ZoneId.systemDefault()); 
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mma")
                .withZone(ZoneId.systemDefault());
        String bookingDate = DATE_TIME_FORMATTER.format(updatedBooking.getStartTime());
        String startTime = TIME_FORMATTER.format(updatedBooking.getStartTime());
        String endTime = TIME_FORMATTER.format(updatedBooking.getEndTime());
        
        assertThat(emailBody).contains(booking.getTitle());
        assertThat(emailBody).contains("will take place on " + bookingDate + " from " + startTime + " to " + endTime + ".");
      
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
    }

    /*
	 * Check that booking that is a tutorial shows topics and tutor name
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    @Transactional
    public void testSendBookingReminderEmail1() throws Exception {
        	
    		// Initialize the database
            userInfoRepository.save(userInfo);
            userInfoRepository.save(tutorUserInfo);
            userInfoRepository.flush();
            topicRepository.saveAndFlush(topic);
            resourceRepository.save(resource1);
            resourceRepository.save(resource2);
            resourceRepository.flush();
            subjectRepository.saveAndFlush(subject);
            
        	// Set booking to accepted and set tutor
        	Set<UserInfo> userInfos = new HashSet<UserInfo>();
        	userInfos.add(userInfo);
        	
        	// Set topic in booking 
        	Set<Topic> topics = new HashSet<Topic>();
        	topics.add(topic);
        	booking.setUserInfos(userInfos);
        	booking.setTutorAccepted(true);
        	booking.setTutorAcceptedId(tutorUserInfo.getId().intValue());
        	booking.setTopics(topics);
    		bookingRepository.saveAndFlush(booking);

            // Get the required entities
            Booking updatedBooking = bookingRepository.findOneWithEagerRelationships(booking.getId()).get();
            User updatedUser = userRepository.findById(user.getId()).get();
            UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
            User tutor = userRepository.findById(updatedBooking.getTutorAcceptedId().longValue()).get();
            Topic updatedTopic = topicRepository.findById(topic.getId()).get();
            mailService.sendBookingReminderEmail(updatedBooking, updatedBooking.getUserInfos(), tutor, "tomorrow");
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo(userInfo.getUser().getEmail());
            assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
            assertThat(message.getContent().toString()).isNotEmpty();
            assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
            
            /* Check booking title, the topic and it's resources are present in email */
            String emailBody = message.getContent().toString();
            
            assertThat(emailBody).contains(booking.getTitle());
            
            assertThat(emailBody).contains(updatedTopic.getTitle());
            
            // Check if the tutor's name is present and dates are correct
            assertThat(emailBody).contains(tutor.getFirstName() + " " + tutor.getLastName());
            
            // Disconnect from session so that the updates on the required entities are not directly saved in db
            em.detach(updatedBooking);
            em.detach(updatedUserInfo);
            em.detach(updatedUser);
            em.detach(tutor);
            em.detach(updatedTopic);
            
        }
    
	/*
	 * Check that booking successfully sends with no tutor present
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    @Transactional
    public void testSendBookingReminderEmail2() throws Exception {   	
    	
		// Initialize the database
        userInfoRepository.save(userInfo);
        userInfoRepository.save(tutorUserInfo);
        userInfoRepository.flush();
        topicRepository.saveAndFlush(topic);
        resourceRepository.save(resource1);
        resourceRepository.save(resource2);
        resourceRepository.flush();
        subjectRepository.saveAndFlush(subject);
        
    	// Set subject to null
    	booking.setSubject(null);
    	
    	// Add requesting user to user infos.
    	Set<UserInfo> userInfos = new HashSet<UserInfo>();
    	userInfos.add(userInfo);
    	
    	// Set user infos
    	booking.setUserInfos(userInfos);
        
		bookingRepository.saveAndFlush(booking);
		
        // Get the required entities
        Booking updatedBooking = bookingRepository.findOneWithEagerRelationships(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Resources is empty since booking has no subject
        List<Resource> resources = new ArrayList<>();
        // Tutor is null since it is not a tutorial
        User tutor = null;

        mailService.sendBookingConfirmedEmail(updatedBooking, updatedBooking.getUserInfos(), tutor, resources);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(userInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title present in email */
        String emailBody = message.getContent().toString();
        
        /* Check email includes strings used for non-tutorial bookings*/

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                .withZone(ZoneId.systemDefault()); 
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mma")
                .withZone(ZoneId.systemDefault());
        String bookingDate = DATE_TIME_FORMATTER.format(updatedBooking.getStartTime());
        String startTime = TIME_FORMATTER.format(updatedBooking.getStartTime());
        String endTime = TIME_FORMATTER.format(updatedBooking.getEndTime());
        
        assertThat(emailBody).contains(booking.getTitle());
        assertThat(emailBody).contains(bookingDate + " from " + startTime + " to " + endTime + ".");
      
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(updatedBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
    }
    
	/*
	 * Check that an edited booking with no tutor sends successfully
	 * Necessary for 100% statement coverage
	 * Necessary for 100% condition coverage
	 */
    @Test
    @Transactional
    public void testSendBookingEditedEmail1() throws Exception {   	
    	
		// Initialize the database
        userInfoRepository.save(userInfo);
        userInfoRepository.save(tutorUserInfo);
        userInfoRepository.flush();
        topicRepository.saveAndFlush(topic);
        resourceRepository.save(resource1);
        resourceRepository.save(resource2);
        resourceRepository.flush();
        subjectRepository.saveAndFlush(subject);
        
    	// Set subject to null
    	booking.setSubject(null);
    	
    	// Add requesting user to user infos.
    	Set<UserInfo> userInfos = new HashSet<UserInfo>();
    	userInfos.add(userInfo);
    	
    	// Set user infos
    	booking.setUserInfos(userInfos);
        
		bookingRepository.saveAndFlush(booking);
		
		bookingEdited.setTitle("Edited Booking");
		bookingEdited.setStartTime(Instant.now().plus(1, ChronoUnit.HOURS));
		bookingEdited.setEndTime(Instant.now().plus(2, ChronoUnit.HOURS));
		
        // Get the required entities
        Booking oldBooking = bookingRepository.findOneWithEagerRelationships(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Resources is empty since booking has no subject
        List<Resource> resources = new ArrayList<>();
        // Tutor is null since it is not a tutorial
        User tutor = null;

        mailService.sendBookingEditedEmail(oldBooking, bookingEdited, oldBooking.getUserInfos(), tutor);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(userInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title present in email */
        String emailBody = message.getContent().toString();
        
        /* Check email includes new title and times */

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                .withZone(ZoneId.systemDefault()); 
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mma")
                .withZone(ZoneId.systemDefault());
        String editedBookingDate = DATE_TIME_FORMATTER.format(bookingEdited.getStartTime());
        String editedStartTime = TIME_FORMATTER.format(bookingEdited.getStartTime());
        String editedEndTime = TIME_FORMATTER.format(bookingEdited.getEndTime());
        
        assertThat(oldBooking.getTitle()).doesNotContain(bookingEdited.getTitle());
        assertThat(emailBody).contains(editedBookingDate);
        assertThat(emailBody).contains(bookingEdited.getTitle());
        assertThat(emailBody).contains(editedStartTime + " to " + editedEndTime);
        
        /* Check email includes old title and times */
        
        String oldBookingDate = DATE_TIME_FORMATTER.format(oldBooking.getStartTime());
        String oldStartTime = TIME_FORMATTER.format(oldBooking.getStartTime());
        String oldEndTIme = TIME_FORMATTER.format(oldBooking.getEndTime());
        
        assertThat(emailBody).contains(oldBookingDate);
        assertThat(emailBody).contains(oldBooking.getTitle());
        assertThat(emailBody).contains(oldStartTime + " to " + oldEndTIme);
      
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(oldBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
    }
    
    /*
	 * Check that an edited booking with no tutor sends successfully
	 */
    @Test
    @Transactional
    public void testSendBookingEditedEmail2() throws Exception {   	
    	
		// Initialize the database
        userInfoRepository.save(userInfo);
        userInfoRepository.save(tutorUserInfo);
        userInfoRepository.flush();
        topicRepository.saveAndFlush(topic);
        resourceRepository.save(resource1);
        resourceRepository.save(resource2);
        resourceRepository.flush();
        subjectRepository.saveAndFlush(subject);
       
    	// Add requesting user to user infos.
    	Set<UserInfo> userInfos = new HashSet<UserInfo>();
    	userInfos.add(userInfo);
    	
    	// Set user infos & topics of booking and edited booking
    	booking.setUserInfos(userInfos);
    	Set<Topic> topics = new HashSet<Topic>();
    	topics.add(topic);
    	booking.setTopics(topics);
		bookingRepository.saveAndFlush(booking);
		
		bookingEdited.setTitle("Edited Booking");
		bookingEdited.setStartTime(Instant.now().plus(1, ChronoUnit.HOURS));
		bookingEdited.setEndTime(Instant.now().plus(2, ChronoUnit.HOURS));
		bookingEdited.setUserInfos(userInfos);
    	topics.add(topic);
    	bookingEdited.setTopics(topics);
		
        // Get the required entities
        Booking oldBooking = bookingRepository.findOneWithEagerRelationships(booking.getId()).get();
        User updatedUser = userRepository.findById(user.getId()).get();
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        User tutor = userRepository.findById(tutorUser.getId()).get();
        // Resources is empty since booking has no subject
        List<Resource> resources = new ArrayList<>();

        mailService.sendBookingEditedEmail(oldBooking, bookingEdited, oldBooking.getUserInfos(), tutor);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(userInfo.getUser().getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        
        /* Check booking title present in email */
        String emailBody = message.getContent().toString();

        /* Check email includes new title and times */

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
                .withZone(ZoneId.systemDefault()); 
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mma")
                .withZone(ZoneId.systemDefault());
        String editedBookingDate = DATE_TIME_FORMATTER.format(bookingEdited.getStartTime());
        String editedStartTime = TIME_FORMATTER.format(bookingEdited.getStartTime());
        String editedEndTime = TIME_FORMATTER.format(bookingEdited.getEndTime());
        
        assertThat(oldBooking.getTitle()).doesNotContain(bookingEdited.getTitle());
        assertThat(emailBody).contains(editedBookingDate);
        assertThat(emailBody).contains(bookingEdited.getTitle());
        assertThat(emailBody).contains(editedStartTime + " to " + editedEndTime);
        
        /* Check email includes old title and times */
        
        String oldBookingDate = DATE_TIME_FORMATTER.format(oldBooking.getStartTime());
        String oldStartTime = TIME_FORMATTER.format(oldBooking.getStartTime());
        String oldEndTIme = TIME_FORMATTER.format(oldBooking.getEndTime());
        
        assertThat(emailBody).contains(oldBookingDate);
        assertThat(emailBody).contains(oldBooking.getTitle());
        assertThat(emailBody).contains(oldStartTime + " to " + oldEndTIme);
        
        /* Check email includes tutor, subject and topic information */
        assertThat(emailBody).contains(tutor.getFirstName() + " " + tutor.getLastName());
        assertThat(emailBody).contains(bookingEdited.getSubject().getTitle());
        assertThat(emailBody).contains(topic.getTitle());
        
        // Disconnect from session so that the updates on the required entities are not directly saved in db
        em.detach(oldBooking);
        em.detach(updatedUserInfo);
        em.detach(updatedUser);
    }

    @Test
    public void testSendEmailWithException() throws Exception {
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
    }

}
