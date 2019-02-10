package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.domain.User;

import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.internet.MimeMessage;

import com.itlc.thelearningzone.domain.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    
    private static final String TUTOR_USER = "tutorUser";

    private static final String BOOKING = "booking";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);

    }
    
    @Async
	public void sendBookingCancelledEmailFromTemplate(Booking booking,User user, User tutorUser, String templateName,
			String titleKey) {
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(TUTOR_USER, tutorUser);
		context.setVariable(BOOKING, booking);
		context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
		String content = templateEngine.process(templateName, context);
		String subject = messageSource.getMessage(titleKey, null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);

	}
    
    @Async
    private void sendBookingAcceptedByTutorFromTemplate(Booking booking, User user, User tutorUser, String templateName,
			String titleKey) {
    	Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(TUTOR_USER, tutorUser);
		context.setVariable(BOOKING, booking);
		context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
		String content = templateEngine.process(templateName, context);
		String subject = messageSource.getMessage(titleKey, null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);	
	}
    
    @Async
    private void sendBookingEditedByAdminFromTemplate(Booking booking, User user, User tutorUser, String templateName,
			String titleKey) {	
    	Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(TUTOR_USER, tutorUser);
		context.setVariable(BOOKING, booking);
		context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
		String content = templateEngine.process(templateName, context);
		String subject = messageSource.getMessage(titleKey, null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);			
	}
    
    @Async
    private void sendBookingRequestRejectedByAdminFromTemplate(Booking booking, User user, String templateName,
			String titleKey) {	
    	Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(BOOKING, booking);
		context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
		String content = templateEngine.process(templateName, context);
		String subject = messageSource.getMessage(titleKey, null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);			
	}

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
    
    @Async
	public void sendBookingCancelledEmail(Booking booking, User user, User tutorUser) {
		log.debug("Sending notification to '{}'", user.getEmail());
		sendBookingCancelledEmailFromTemplate(booking, user, tutorUser, "mail/cancellationEmail", "email.cancelled.title");
	}
    
    @Async
	public void sendBookingAcceptedByTutorEmail(Booking booking,User user,User tutorUser) {
		log.debug("Sending notification to '{}'", user.getEmail());
		sendBookingAcceptedByTutorFromTemplate(booking, user, tutorUser,"mail/bookingAcceptedByTutorEmail", "email.accepted.title");
	}
     
    @Async
	public void sendBookingEditedyAdminEmail(Booking booking, User user, User tutorUser) {
		log.debug("Sending notification to '{}'", user.getEmail());
		sendBookingEditedByAdminFromTemplate(booking, user, tutorUser, "mail/bookingEditedByAdminEmail", "email.edit.title");
	}
    
    @Async
   	public void sendBookingRequestRejectedByAdminEmail(Booking booking, User user) {
   		log.debug("Sending notification to '{}'", user.getEmail());
   		sendBookingRequestRejectedByAdminFromTemplate(booking, user, "mail/bookingRequestRejectedByAdminEmail", "email.reject.title");
   	}

	
	
}
