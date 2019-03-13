package com.itlc.thelearningzone.config;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class AcademicCalendarConfiguration {

	private static AcademicCalendarConfiguration INSTANCE;
    private String startDate;
     
    private AcademicCalendarConfiguration() {        
    }
     
    public static AcademicCalendarConfiguration getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AcademicCalendarConfiguration();
        }
        return INSTANCE;
    }

	public static AcademicCalendarConfiguration getINSTANCE() {
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
