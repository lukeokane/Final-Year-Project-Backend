package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.domain.Resource;
import com.itlc.thelearningzone.service.dto.ResourceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Resource.
 */
public interface ResourceService {
	
	/**
	 * Get all the resources associated with a subject
	 * 
	 * @param subjectId the id of the subject to retrieve all resources for
	 * @return the list of entities
	 */
	List<Resource> findAllResourcesInSubject(Long subjectId);
	
    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save
     * @return the persisted entity
     */
    ResourceDTO save(ResourceDTO resourceDTO);

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ResourceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" resource.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ResourceDTO> findOne(Long id);

    /**
     * Delete the "id" resource.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
