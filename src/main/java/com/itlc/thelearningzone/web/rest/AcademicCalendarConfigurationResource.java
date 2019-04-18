package com.itlc.thelearningzone.web.rest;

import java.io.File;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.itlc.thelearningzone.config.AcademicCalendarConfiguration;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Academic Calendar Configuration settings.
 */
@RestController
@RequestMapping("/api")
public class AcademicCalendarConfigurationResource {

	private final Logger log = LoggerFactory.getLogger(AcademicCalendarConfigurationResource.class);

	private static final String ENTITY_NAME = "academicCalendarConfiguration";

	private static final String FILE_NOT_FOUND_ERROR_MESSAGE = "The settings file could not be found.";

	public AcademicCalendarConfigurationResource() {

	}

	@GetMapping("/academicCalendarConfiguration/getStartDate")
	@Timed
	public ResponseEntity<String> getAcademicCalendarStartDate() {
		log.debug("REST request to get the start date of the current academic calendar");
		
		AcademicCalendarConfiguration academicCalendarConfiguration = AcademicCalendarConfiguration.getInstance();
		try {
			File academicCalendarSettingsFile = new File("academicCalendarConfiguration.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(AcademicCalendarConfiguration.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			academicCalendarConfiguration = (AcademicCalendarConfiguration) jaxbUnmarshaller.unmarshal(academicCalendarSettingsFile);
		} catch (JAXBException e) {
			log.debug(FILE_NOT_FOUND_ERROR_MESSAGE, e);
			return new ResponseEntity<>(FILE_NOT_FOUND_ERROR_MESSAGE, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(academicCalendarConfiguration.getStartDate(), HttpStatus.OK);
	}

	@PutMapping("/academicCalendarConfiguration/editStartDate/{startDate}")
	@Timed
	public ResponseEntity<String> updateAcademicCalendarStartDate(@PathVariable String startDate)
			throws URISyntaxException {
		log.debug("REST request to save AcademicCalendarConfiguration start date as : {}", startDate);
		
		AcademicCalendarConfiguration academicCalendarConfiguration = AcademicCalendarConfiguration.getInstance();
		String newStartDate = startDate;
		academicCalendarConfiguration.setStartDate(newStartDate);
		try {
			File academicCalendarSettingsFile = new File("academicCalendarConfiguration.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(AcademicCalendarConfiguration.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(academicCalendarConfiguration, academicCalendarSettingsFile);

		} catch (JAXBException e) {
			log.debug(FILE_NOT_FOUND_ERROR_MESSAGE, e);
			return new ResponseEntity<>(FILE_NOT_FOUND_ERROR_MESSAGE, HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, newStartDate))
				.body(newStartDate);
	}

}
