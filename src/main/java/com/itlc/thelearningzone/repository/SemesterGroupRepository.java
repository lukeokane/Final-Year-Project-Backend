package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.SemesterGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SemesterGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemesterGroupRepository extends JpaRepository<SemesterGroup, Long> {

    @Query(value = "select distinct semester_group from SemesterGroup semester_group left join fetch semester_group.subjects",
        countQuery = "select count(distinct semester_group) from SemesterGroup semester_group")
    Page<SemesterGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct semester_group from SemesterGroup semester_group left join fetch semester_group.subjects")
    List<SemesterGroup> findAllWithEagerRelationships();

    @Query("select semester_group from SemesterGroup semester_group left join fetch semester_group.subjects where semester_group.id =:id")
    Optional<SemesterGroup> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "SELECT semesterGroup from SemesterGroup semesterGroup left join semesterGroup.semester semester WHERE semester.id IN ("
    		+ "SELECT semester.id FROM CourseYear courseYear LEFT JOIN courseYear.semesters semester WHERE courseYear.id = :courseYearId AND semester.semesterStartDate <= :startTime AND semester.semesterEndDate >= :endTime)"
    		+ "ORDER by semesterGroup.title")
        Page<SemesterGroup> findAllSemesterGroupsInCourseYearInTimeFrame(Pageable pageable, @Param("courseYearId") Long courseYearId, @Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);
}