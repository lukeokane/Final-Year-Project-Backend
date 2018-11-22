package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.CourseYearService;
import com.itlc.thelearningzone.domain.CourseYear;
import com.itlc.thelearningzone.repository.CourseYearRepository;
import com.itlc.thelearningzone.service.dto.CourseYearDTO;
import com.itlc.thelearningzone.service.mapper.CourseYearMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CourseYear.
 */
@Service
@Transactional
public class CourseYearServiceImpl implements CourseYearService {

    private final Logger log = LoggerFactory.getLogger(CourseYearServiceImpl.class);

    private final CourseYearRepository courseYearRepository;

    private final CourseYearMapper courseYearMapper;

    public CourseYearServiceImpl(CourseYearRepository courseYearRepository, CourseYearMapper courseYearMapper) {
        this.courseYearRepository = courseYearRepository;
        this.courseYearMapper = courseYearMapper;
    }

    /**
     * Save a courseYear.
     *
     * @param courseYearDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CourseYearDTO save(CourseYearDTO courseYearDTO) {
        log.debug("Request to save CourseYear : {}", courseYearDTO);

        CourseYear courseYear = courseYearMapper.toEntity(courseYearDTO);
        courseYear = courseYearRepository.save(courseYear);
        return courseYearMapper.toDto(courseYear);
    }

    /**
     * Get all the courseYears.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseYearDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseYears");
        return courseYearRepository.findAll(pageable)
            .map(courseYearMapper::toDto);
    }


    /**
     * Get one courseYear by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseYearDTO> findOne(Long id) {
        log.debug("Request to get CourseYear : {}", id);
        return courseYearRepository.findById(id)
            .map(courseYearMapper::toDto);
    }

    /**
     * Delete the courseYear by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseYear : {}", id);
        courseYearRepository.deleteById(id);
    }
}
