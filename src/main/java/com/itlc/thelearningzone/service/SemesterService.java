package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.SemesterDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Semester.
 */
public interface SemesterService {

    /**
     * Save a semester.
     *
     * @param semesterDTO the entity to save
     * @return the persisted entity
     */
    SemesterDTO save(SemesterDTO semesterDTO);

    /**
     * Get all the semesters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SemesterDTO> findAll(Pageable pageable);

    /**
     * Get all the Semester with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<SemesterDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" semester.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SemesterDTO> findOne(Long id);

    /**
     * Delete the "id" semester.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
