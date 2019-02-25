package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.service.dto.NotificationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Notification.
 */
public interface NotificationService {
	
    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save
     * @return the persisted entity
     */
    NotificationDTO save(NotificationDTO notificationDTO);

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NotificationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NotificationDTO> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	Page<NotificationDTO> findAllDateAsc(Pageable pageable);

	//List<NotificationDTO> findAllNotificationsList();
	
	/**
	 * Get all notifications received by a user after with a timestamp...
	 * equal or greater than @param startTime
	 * 
	 * @param pageable the pagination information
	 * @param userId the user ID to get notifications for
	 * @param startTime the time to get notifications after
	 * 
	 */
	Page<NotificationDTO> findUserNotificationsAfterTime(Pageable pageable, Long userId, Instant startTime );

}
