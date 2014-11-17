package com.fahad.ornob.sust.hitthedeal.item;

public class Event {
	int eventId, creatorId;
	String eventDescription;
	long startDate, endDate;
	double latitude, longitude;

	public Event() {
		// TODO Auto-generated constructor stub
	}

	public Event(int eventId, int creatorId, String eventDescription,
			long startDate, long endDate, double latitude, double longitude) {
		this.eventId = eventId;
		this.creatorId = creatorId;
		this.eventDescription = eventDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
