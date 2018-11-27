package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.SemesterGroupService;
import com.itlc.thelearningzone.domain.SemesterGroup;
import com.itlc.thelearningzone.repository.SemesterGroupRepository;
import com.itlc.thelearningzone.service.dto.SemesterGroupDTO;
import com.itlc.thelearningzone.service.mapper.SemesterGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SemesterGroup.
 */
@Service
@Transactional
public class SemesterGroupServiceImpl implements SemesterGroupService {

    private final Logger log = LoggerFactory.getLogger(SemesterGroupServiceImpl.class);

    private final SemesterGroupRepository semesterGroupRepository;

    private final SemesterGroupMapper semesterGroupMapper;

    public SemesterGroupServiceImpl(SemesterGroupRepository semesterGroupRepository, SemesterGroupMapper semesterGroupMapper) {
        this.semesterGroupRepository = semesterGroupRepository;
        this.semesterGroupMapper = semesterGroupMapper;
    }

    /**
     * Save a semesterGroup.
     *
     * @param semesterGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SemesterGroupDTO save(SemesterGroupDTO semesterGroupDTO) {
        log.debug("Request to save SemesterGroup : {}", semesterGroupDTO);

        SemesterGroup semesterGroup = semesterGroupMapper.toEntity(semesterGroupDTO);
        semesterGroup = semesterGroupRepository.save(semesterGroup);
        return semesterGroupMapper.toDto(semesterGroup);
    }

    /**
     * Get all the semesterGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SemesterGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SemesterGroups");
        return semesterGroupRepository.findAll(pageable)
            .map(semesterGroupMapper::toDto);
    }

    /**
     * Get all the SemesterGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<SemesterGroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return semesterGroupRepository.findAllWithEagerRelationships(pageable).map(semesterGroupMapper::toDto);
    }
    

    /**
     * Get one semesterGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SemesterGroupDTO> findOne(Long id) {
        log.debug("Request to get SemesterGroup : {}", id);
        return semesterGroupRepository.findOneWithEagerRelationships(id)
            .map(semesterGroupMapper::toDto);
    }

    /**
     * Delete the semesterGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SemesterGroup : {}", id);
        semesterGroupRepository.deleteById(id);
    }
}
