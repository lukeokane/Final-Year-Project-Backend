package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.MessageDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Message.
 */
public interface MessageService {

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save
     * @return the persisted entity
     */
    MessageDTO save(MessageDTO messageDTO);

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MessageDTO> findAll(Pageable pageable);
    
    /**
     * Get all the messages by tag.
     *
     * @param tag the type of message response required
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MessageDTO> findAllByTag(String tag, Pageable pageable);


    /**
     * Get the "id" message.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MessageDTO> findOne(Long id);

    /**
     * Delete the "id" message.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
