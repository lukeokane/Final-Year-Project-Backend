package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.Resource;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Resource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

	@Query(value = "SELECT resource FROM Resource resource LEFT JOIN resource.topic topic WHERE topic.id IN (SELECT topic.id FROM Subject subject LEFT JOIN subject.topics topic WHERE subject.id = :subjectId) ORDER BY topic.id")
	List<Resource>findAllResourcesInSubject(@Param("subjectId") Long subjectId);
}
