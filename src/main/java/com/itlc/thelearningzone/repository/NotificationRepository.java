package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.Notification;
import com.itlc.thelearningzone.service.dto.NotificationDTO;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("select notification from Notification notification where notification.receiver.user.login = ?#{principal.username} order by notification.timestamp asc")
	Page<Notification> getAllTest(Pageable pageable);
	
	@Query("select notification from Notification notification where notification.receiver.user.id = :userId and notification.timestamp >= :startTime order by notification.timestamp asc")
	Page<Notification> findUserNotificationsAfterTime(Pageable pageable, @Param("userId") Long userId,  @Param("startTime") Instant startTime);

}
