package com.itlc.thelearningzone.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itlc.thelearningzone.service.CourseYearService;
import com.itlc.thelearningzone.web.rest.errors.BadRequestAlertException;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;
import com.itlc.thelearningzone.web.rest.util.PaginationUtil;
import com.itlc.thelearningzone.service.dto.CourseYearDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CourseYear.
 */
@RestController
@RequestMapping("/api")
public class CourseYearResource {

    private final Logger log = LoggerFactory.getLogger(CourseYearResource.class);

    private static final String ENTITY_NAME = "courseYear";

    private final CourseYearService courseYearService;

    public CourseYearResource(CourseYearService courseYearService) {
        this.courseYearService = courseYearService;
    }

    /**
     * POST  /course-years : Create a new courseYear.
     *
     * @param courseYearDTO the courseYearDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseYearDTO, or with status 400 (Bad Request) if the courseYear has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-years")
    @Timed
    public ResponseEntity<CourseYearDTO> createCourseYear(@Valid @RequestBody CourseYearDTO courseYearDTO) throws URISyntaxException {
        log.debug("REST request to save CourseYear : {}", courseYearDTO);
        if (courseYearDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseYear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseYearDTO result = courseYearService.save(courseYearDTO);
        return ResponseEntity.created(new URI("/api/course-years/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-years : Updates an existing courseYear.
     *
     * @param courseYearDTO the courseYearDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseYearDTO,
     * or with status 400 (Bad Request) if the courseYearDTO is not valid,
     * or with status 500 (Internal Server Error) if the courseYearDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-years")
    @Timed
    public ResponseEntity<CourseYearDTO> updateCourseYear(@Valid @RequestBody CourseYearDTO courseYearDTO) throws URISyntaxException {
        log.debug("REST request to update CourseYear : {}", courseYearDTO);
        if (courseYearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseYearDTO result = courseYearService.save(courseYearDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseYearDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-years : get all the courseYears.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseYears in body
     */
    @GetMapping("/course-years")
    @Timed
    public ResponseEntity<List<CourseYearDTO>> getAllCourseYears(Pageable pageable) {
        log.debug("REST request to get a page of CourseYears");
        Page<CourseYearDTO> page = courseYearService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-years");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /course-years/:id : get the "id" courseYear.
     *
     * @param id the id of the courseYearDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseYearDTO, or with status 404 (Not Found)
     */
    @GetMapping("/course-years/{id}")
    @Timed
    public ResponseEntity<CourseYearDTO> getCourseYear(@PathVariable Long id) {
        log.debug("REST request to get CourseYear : {}", id);
        Optional<CourseYearDTO> courseYearDTO = courseYearService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseYearDTO);
    }

    /**
     * DELETE  /course-years/:id : delete the "id" courseYear.
     *
     * @param id the id of the courseYearDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-years/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseYear(@PathVariable Long id) {
        log.debug("REST request to delete CourseYear : {}", id);
        courseYearService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
