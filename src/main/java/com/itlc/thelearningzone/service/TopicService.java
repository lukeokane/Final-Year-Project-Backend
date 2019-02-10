package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.TopicDTO;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Topic.
 */
public interface TopicService {

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save
     * @return the persisted entity
     */
    TopicDTO save(TopicDTO topicDTO);

    /**
     * Get all the topics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TopicDTO> findAll(Pageable pageable);


    /**
     * Get the "id" topic.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TopicDTO> findOne(Long id);

    /**
     * Delete the "id" topic.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Get all the topics by subject ID.
     *
     * @param array of subject id by semester group
     * @return the list of entities
     */
    List<TopicDTO> findTopicsList(Long[] subjectsId);
}
