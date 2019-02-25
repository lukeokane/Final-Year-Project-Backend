package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.NotificationService;
import com.itlc.thelearningzone.domain.Notification;
import com.itlc.thelearningzone.repository.NotificationRepository;
import com.itlc.thelearningzone.service.dto.NotificationDTO;
import com.itlc.thelearningzone.service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Notification.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);

        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable)
            .map(notificationMapper::toDto);
    }
    
    /**
     * Get all the  notifications by user logged in
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
	@Transactional(readOnly = true)
	public Page<NotificationDTO> findAllDateAsc(Pageable pageable) {
		log.debug("Request to get all Notifications");
        return notificationRepository.getAllTest(pageable)
            .map(notificationMapper::toDto);
	}


    /**
     * Get one notification by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id)
            .map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

//	@Override
//	public List<NotificationDTO> findAllNotificationsList() {
//		List<NotificationDTO> list = new ArrayList<NotificationDTO>();
//		List<Notification> ps = notificationRepository.findAllList();
//		for (Notification p : ps)
//			list.add(notificationMapper.toDto(p));
//
//		return list;
//	}
	
	@Override
	public Page<NotificationDTO> findUserNotificationsAfterTime(Pageable pageable, Long userId, Instant startTime) {
		log.debug("Request to get notifications for user {} after time {}", userId, startTime);
		return notificationRepository.findUserNotificationsAfterTime(pageable, userId, startTime).map(notificationMapper::toDto);
	}
	

}
