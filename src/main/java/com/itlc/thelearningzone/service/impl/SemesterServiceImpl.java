package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.SemesterService;
import com.itlc.thelearningzone.domain.Semester;
import com.itlc.thelearningzone.repository.SemesterRepository;
import com.itlc.thelearningzone.service.dto.SemesterDTO;
import com.itlc.thelearningzone.service.mapper.SemesterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Semester.
 */
@Service
@Transactional
public class SemesterServiceImpl implements SemesterService {

    private final Logger log = LoggerFactory.getLogger(SemesterServiceImpl.class);

    private final SemesterRepository semesterRepository;

    private final SemesterMapper semesterMapper;

    public SemesterServiceImpl(SemesterRepository semesterRepository, SemesterMapper semesterMapper) {
        this.semesterRepository = semesterRepository;
        this.semesterMapper = semesterMapper;
    }

    /**
     * Save a semester.
     *
     * @param semesterDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SemesterDTO save(SemesterDTO semesterDTO) {
        log.debug("Request to save Semester : {}", semesterDTO);

        Semester semester = semesterMapper.toEntity(semesterDTO);
        semester = semesterRepository.save(semester);
        return semesterMapper.toDto(semester);
    }

    /**
     * Get all the semesters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SemesterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Semesters");
        return semesterRepository.findAll(pageable)
            .map(semesterMapper::toDto);
    }


    /**
     * Get one semester by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SemesterDTO> findOne(Long id) {
        log.debug("Request to get Semester : {}", id);
        return semesterRepository.findById(id)
            .map(semesterMapper::toDto);
    }

    /**
     * Delete the semester by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Semester : {}", id);
        semesterRepository.deleteById(id);
    }
}
