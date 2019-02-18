package com.itlc.thelearningzone.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itlc.thelearningzone.service.SemesterGroupService;
import com.itlc.thelearningzone.web.rest.errors.BadRequestAlertException;
import com.itlc.thelearningzone.web.rest.util.HeaderUtil;
import com.itlc.thelearningzone.web.rest.util.PaginationUtil;
import com.itlc.thelearningzone.service.dto.SemesterGroupDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SemesterGroup.
 */
@RestController
@RequestMapping("/api")
public class SemesterGroupResource {

    private final Logger log = LoggerFactory.getLogger(SemesterGroupResource.class);

    private static final String ENTITY_NAME = "semesterGroup";

    private final SemesterGroupService semesterGroupService;

    public SemesterGroupResource(SemesterGroupService semesterGroupService) {
        this.semesterGroupService = semesterGroupService;
    }

    /**
     * POST  /semester-groups : Create a new semesterGroup.
     *
     * @param semesterGroupDTO the semesterGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new semesterGroupDTO, or with status 400 (Bad Request) if the semesterGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/semester-groups")
    @Timed
    public ResponseEntity<SemesterGroupDTO> createSemesterGroup(@Valid @RequestBody SemesterGroupDTO semesterGroupDTO) throws URISyntaxException {
        log.debug("REST request to save SemesterGroup : {}", semesterGroupDTO);
        if (semesterGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new semesterGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SemesterGroupDTO result = semesterGroupService.save(semesterGroupDTO);
        return ResponseEntity.created(new URI("/api/semester-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /semester-groups : Updates an existing semesterGroup.
     *
     * @param semesterGroupDTO the semesterGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated semesterGroupDTO,
     * or with status 400 (Bad Request) if the semesterGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the semesterGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/semester-groups")
    @Timed
    public ResponseEntity<SemesterGroupDTO> updateSemesterGroup(@Valid @RequestBody SemesterGroupDTO semesterGroupDTO) throws URISyntaxException {
        log.debug("REST request to update SemesterGroup : {}", semesterGroupDTO);
        if (semesterGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SemesterGroupDTO result = semesterGroupService.save(semesterGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, semesterGroupDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /semester-groups : get all the semesterGroups.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of semesterGroups in body
     */
    @GetMapping("/semester-groups/currentlyRunning")
    @Timed
    public ResponseEntity<List<SemesterGroupDTO>> getBookingsLatestDetailsChanges(Pageable pageable, @RequestParam(required = true) Long courseYearId) {
        LocalDate startTime = LocalDate.now();
        LocalDate endTime = LocalDate.now();
    	
    	log.debug("REST request to get SemesterGroups for course year {} starting at time {} to end time {}", courseYearId, startTime, endTime);
        
    	Page<SemesterGroupDTO> page;
 
        page = semesterGroupService.findAllSemesterGroupsInCourseYearInTimeFrame(pageable, courseYearId, startTime, endTime);
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/semester-groups?courseYearId=%d", courseYearId));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /semester-groups : get all the semesterGroups.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of semesterGroups in body
     */
    @GetMapping("/semester-groups")
    @Timed
    public ResponseEntity<List<SemesterGroupDTO>> getAllSemesterGroups(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of SemesterGroups");
        Page<SemesterGroupDTO> page;
        if (eagerload) {
            page = semesterGroupService.findAllWithEagerRelationships(pageable);
        } else {
            page = semesterGroupService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/semester-groups?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /semester-groups/:id : get the "id" semesterGroup.
     *
     * @param id the id of the semesterGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the semesterGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/semester-groups/{id}")
    @Timed
    public ResponseEntity<SemesterGroupDTO> getSemesterGroup(@PathVariable Long id) {
        log.debug("REST request to get SemesterGroup : {}", id);
        Optional<SemesterGroupDTO> semesterGroupDTO = semesterGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semesterGroupDTO);
    }

    /**
     * DELETE  /semester-groups/:id : delete the "id" semesterGroup.
     *
     * @param id the id of the semesterGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/semester-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteSemesterGroup(@PathVariable Long id) {
        log.debug("REST request to delete SemesterGroup : {}", id);
        semesterGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
