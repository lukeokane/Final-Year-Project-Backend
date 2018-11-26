package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.SemesterGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SemesterGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemesterGroupRepository extends JpaRepository<SemesterGroup, Long> {

}
