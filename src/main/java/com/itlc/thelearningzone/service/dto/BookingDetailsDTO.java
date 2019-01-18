package com.itlc.thelearningzone.service.dto;

import java.io.Serializable;

/**
 * A DTO for including the Booking's actual Subject object.
 */
public class BookingDetailsDTO implements Serializable {

   public BookingDTO booking;
   
   public SubjectDTO subject;
}
