package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.SubjectService;
import com.itlc.thelearningzone.domain.Subject;
import com.itlc.thelearningzone.repository.SubjectRepository;
import com.itlc.thelearningzone.service.dto.SubjectDTO;
import com.itlc.thelearningzone.service.mapper.SubjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Subject.
 */
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final SubjectRepository subjectRepository;

    private final SubjectMapper subjectMapper;

    public SubjectServiceImpl(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    /**
     * Save a subject.
     *
     * @param subjectDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SubjectDTO save(SubjectDTO subjectDTO) {
        log.debug("Request to save Subject : {}", subjectDTO);

        Subject subject = subjectMapper.toEntity(subjectDTO);
        subject = subjectRepository.save(subject);
        return subjectMapper.toDto(subject);
    }

    /**
     * Get all the subjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subjects");
        return subjectRepository.findAll(pageable)
            .map(subjectMapper::toDto);
    }

    /**
     * Get all the Subject with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<SubjectDTO> findAllWithEagerRelationships(Pageable pageable) {
        return subjectRepository.findAllWithEagerRelationships(pageable).map(subjectMapper::toDto);
    }
    

    /**
     * Get one subject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectDTO> findOne(Long id) {
        log.debug("Request to get Subject : {}", id);
        return subjectRepository.findOneWithEagerRelationships(id)
            .map(subjectMapper::toDto);
    }

    /**
     * Delete the subject by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subject : {}", id);
        subjectRepository.deleteById(id);
    }

    /**
     * Get all the Subjects in a list
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
	public List<SubjectDTO> findAllSubjectsList() {
		List<SubjectDTO> list = new ArrayList<SubjectDTO>();
		List<Subject> ps = subjectRepository.findAll();
		for (Subject p : ps)
			list.add(subjectMapper.toDto(p));

		return list;
	}
}
