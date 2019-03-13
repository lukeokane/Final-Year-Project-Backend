package com.itlc.thelearningzone.config;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class AcademicCalendarConfiguration {

	private static final AcademicCalendarConfiguration INSTANCE =
			new AcademicCalendarConfiguration();
    private String startDate;
     
    private AcademicCalendarConfiguration() {        
    }
     
    public static AcademicCalendarConfiguration getInstance() {
        return INSTANCE;
    }

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "AcademicCalendarConfiguration [startDate=" + startDate + "]";
	}
    
}
