package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	
	@Query("select distinct message from Message message where message.tag =:tag")
	Page<Message> findAllByTag(@Param("tag") String tag, Pageable pageable);

}
