package com.itlc.thelearningzone.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Scope;

@XmlRootElement
@Scope("singleton")
public class AcademicYear {

	String startDate;

	public String getStartDate() {
		return startDate;
	}

	@XmlElement
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String toString() {
		return "AcademicYear [startDate=" + startDate + "]";
	}

}
