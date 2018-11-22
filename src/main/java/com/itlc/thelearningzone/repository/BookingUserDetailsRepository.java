package com.itlc.thelearningzone.repository;

import com.itlc.thelearningzone.domain.BookingUserDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BookingUserDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingUserDetailsRepository extends JpaRepository<BookingUserDetails, Long> {

}
