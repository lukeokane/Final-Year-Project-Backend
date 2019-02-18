package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.SemesterGroupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing SemesterGroup.
 */
public interface SemesterGroupService {

    /**
     * Save a semesterGroup.
     *
     * @param semesterGroupDTO the entity to save
     * @return the persisted entity
     */
    SemesterGroupDTO save(SemesterGroupDTO semesterGroupDTO);
    
    /**
     * Get semester groups within a course year between two dates
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Page<SemesterGroupDTO> findAllSemesterGroupsInCourseYearInTimeFrame(Pageable pageable, Long courseYearId, LocalDate startTime, LocalDate endTime);

    /**
     * Get all the semesterGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SemesterGroupDTO> findAll(Pageable pageable);

    /**
     * Get all the SemesterGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<SemesterGroupDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" semesterGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SemesterGroupDTO> findOne(Long id);

    /**
     * Delete the "id" semesterGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
