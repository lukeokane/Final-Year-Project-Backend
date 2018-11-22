package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Semester entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    @Query(value = "select distinct semester from Semester semester left join fetch semester.subjects left join fetch semester.userInfos",
        countQuery = "select count(distinct semester) from Semester semester")
    Page<Semester> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct semester from Semester semester left join fetch semester.subjects left join fetch semester.userInfos")
    List<Semester> findAllWithEagerRelationships();

    @Query("select semester from Semester semester left join fetch semester.subjects left join fetch semester.userInfos where semester.id =:id")
    Optional<Semester> findOneWithEagerRelationships(@Param("id") Long id);

}
