package databaseEntities;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
	int eventId, creatorId;
	String eventDescription, eventName, eventImg, eventUrl;
	long startDate, endDate;
	double latitude, longitude;

	public Event() {
		// TODO Auto-generated constructor stub
	}

	private Event(Parcel source) {
		this.eventId = source.readInt();
		this.creatorId = source.readInt();
		this.eventName = source.readString();
		this.eventDescription = source.readString();
		this.eventImg = source.readString();
		this.eventUrl = source.readString();
		this.startDate = source.readLong();
		this.endDate = source.readLong();
		this.latitude = source.readDouble();
		this.longitude = source.readDouble();
	}

	public Event(int eventId, int creatorId, String eventDescription,
			long startDate, long endDate, double latitude, double longitude,
			String eventName, String eventImg, String eventUrl) {
		this.eventId = eventId;
		this.creatorId = creatorId;
		this.eventDescription = eventDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.eventName = eventName;
		this.eventImg = eventImg;
		this.eventUrl = eventUrl;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventImg() {
		return eventImg;
	}
	
	public void setEventImg(String eventImg) {
		this.eventImg = eventImg;
	}

	public String getEventUrl() {
		return eventUrl;
	}

	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel destination, int flags) {
		destination.writeInt(eventId);
		destination.writeInt(creatorId);
		destination.writeString(eventName);
		destination.writeString(eventDescription);
		destination.writeString(eventImg);
		destination.writeString(eventUrl);
		destination.writeLong(startDate);
		destination.writeLong(endDate);
		destination.writeDouble(latitude);
		destination.writeDouble(longitude);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@SuppressWarnings("rawtypes")
		@Override
		public databaseEntities.Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}
	};
}
