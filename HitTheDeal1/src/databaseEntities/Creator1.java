package databaseEntities;



import android.os.Parcel;
import android.os.Parcelable;

public class Creator1 implements Parcelable {
	int userId, creatorTypeId;
	String userName, address, email, phnNo;
	long dateOfCreation;
	double latitude, longitude;
	String imageUrl;

	public Creator1() {
		// TODO Auto-generated constructor stub
	}

	private Creator1(Parcel source) {
		this.userId = source.readInt();
		this.userName = source.readString();
		this.address = source.readString();
		this.email = source.readString();
		this.phnNo = source.readString();
		this.dateOfCreation = source.readLong();
		this.latitude = source.readDouble();
		this.longitude = source.readDouble();
		this.imageUrl = source.readString();
		this.creatorTypeId = source.readInt();
	}

	public Creator1(int userId, String userName, String address, String email,
			String phnNo, long dateOfCreation, double latitude,
			double longitude, String imageUrl, int creatorTypeId) {
		this.userId = userId;
		this.userName = userName;
		this.address = address;
		this.email = email;
		this.phnNo = phnNo;
		this.dateOfCreation = dateOfCreation;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
		this.creatorTypeId = creatorTypeId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCreatorTypeId() {
		return creatorTypeId;
	}

	public String getCreatorType() {
		switch (creatorTypeId) {
		case 1:
			return "Local Business";
		case 2:
			return "Restaurant";

		case 3:
			return "Cultural";

		case 4:
			return "Cause";

		default:
			return "Other";

		}
	}

	public void setCreatorTypeId(int creatorTypeId) {
		this.creatorTypeId = creatorTypeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhnNo() {
		return phnNo;
	}

	public void setPhnNo(String phnNo) {
		this.phnNo = phnNo;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel destination, int flags) {
		destination.writeInt(userId);
		destination.writeString(userName);
		destination.writeString(address);
		destination.writeString(email);
		destination.writeString(phnNo);
		destination.writeLong(dateOfCreation);
		destination.writeDouble(latitude);
		destination.writeDouble(longitude);
		destination.writeString(imageUrl);
		destination.writeInt(creatorTypeId);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@SuppressWarnings("rawtypes")
		@Override
		public databaseEntities.Creator1 createFromParcel(Parcel in) {
			return new databaseEntities.Creator1(in);
		}

		@Override
		public Creator[] newArray(int size) {
			return new Creator[size];
		}
	};

}
