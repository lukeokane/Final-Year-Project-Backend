package com.itlc.thelearningzone.service.dto;

import java.io.Serializable;

/**
 * A DTO for including the Booking's actual Subject object.
 */
public class BookingDetailsDTO implements Serializable {

   private BookingDTO booking;
   
   private SubjectDTO subject;

	public BookingDTO getBooking() {
		return booking;
	}
	
	public void setBooking(BookingDTO booking) {
		this.booking = booking;
	}
	
	public SubjectDTO getSubject() {
		return subject;
	}
	
	public void setSubject(SubjectDTO subject) {
		this.subject = subject;
	}
   
   
}
