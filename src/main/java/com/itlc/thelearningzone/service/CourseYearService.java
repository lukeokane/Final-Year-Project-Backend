package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.CourseYearDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CourseYear.
 */
public interface CourseYearService {

    /**
     * Save a courseYear.
     *
     * @param courseYearDTO the entity to save
     * @return the persisted entity
     */
    CourseYearDTO save(CourseYearDTO courseYearDTO);

    /**
     * Get all the courseYears.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CourseYearDTO> findAll(Pageable pageable);


    /**
     * Get the "id" courseYear.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CourseYearDTO> findOne(Long id);

    /**
     * Delete the "id" courseYear.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
