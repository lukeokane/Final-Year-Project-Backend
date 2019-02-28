package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.CourseYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CourseYear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseYearRepository extends JpaRepository<CourseYear, Long> {

    @Query(value = "select distinct course_year from CourseYear course_year left join fetch course_year.subjects",
        countQuery = "select count(distinct course_year) from CourseYear course_year")
    Page<CourseYear> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct course_year from CourseYear course_year left join fetch course_year.subjects")
    List<CourseYear> findAllWithEagerRelationships();

    @Query("select course_year from CourseYear course_year left join fetch course_year.subjects where course_year.id =:id")
    Optional<CourseYear> findOneWithEagerRelationships(@Param("id") Long id);

}
