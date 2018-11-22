package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.CourseYear;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseYear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseYearRepository extends JpaRepository<CourseYear, Long> {

}
