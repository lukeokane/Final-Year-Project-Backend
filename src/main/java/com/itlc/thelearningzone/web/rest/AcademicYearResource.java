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
import com.itlc.thelearningzone.domain.AcademicYear;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Academic Year settings.
 */
@RestController
@RequestMapping("/api")
public class AcademicYearResource {

	private final Logger log = LoggerFactory.getLogger(AcademicYearResource.class);

	private static final String ENTITY_NAME = "academicYear";

	private static final String FILE_NOT_FOUND_ERROR_MESSAGE = "The settings file could not be found.";

	public AcademicYearResource() {

	}

	@GetMapping("/academicYear/getStartDate")
	@Timed
	public ResponseEntity<String> getAcademicYearStartDate() {
		log.debug("REST request to get the start date of the current academic year");
		AcademicYear academicYear = new AcademicYear();

		try {

			File academicYearSettingsFile = new File("academicYearSettings.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(AcademicYear.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			academicYear = (AcademicYear) jaxbUnmarshaller.unmarshal(academicYearSettingsFile);
			System.out.println(academicYear);

		} catch (JAXBException e) {
			log.debug(FILE_NOT_FOUND_ERROR_MESSAGE, e);
			return new ResponseEntity<>(FILE_NOT_FOUND_ERROR_MESSAGE, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(academicYear.getStartDate(), HttpStatus.OK);
	}

	@PutMapping("/academicYear/editStartDate/{startDate}")
	@Timed
	public ResponseEntity<String> updateAcademicYearStartDate(@PathVariable String startDate)
			throws URISyntaxException {
		log.debug("REST request to save AcademicYear start date as : {}", startDate);
		AcademicYear academicYear = new AcademicYear();
		academicYear.setStartDate(startDate);

		try {

			File academicYearSettingsFile = new File("academicYearSettings.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(AcademicYear.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(academicYear, academicYearSettingsFile);
			jaxbMarshaller.marshal(academicYear, System.out);

		} catch (JAXBException e2) {
			log.debug(FILE_NOT_FOUND_ERROR_MESSAGE, e2);
			return new ResponseEntity<>(FILE_NOT_FOUND_ERROR_MESSAGE, HttpStatus.CONFLICT);
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, startDate)).body(startDate);
	}

}
