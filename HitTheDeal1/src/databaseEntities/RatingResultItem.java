package databaseEntities;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingResultItem implements Parcelable {

	double rating;
	int countNumber;

	private RatingResultItem(Parcel source) {
		this.rating = source.readDouble();
		this.countNumber = source.readInt();
	}

	public RatingResultItem(double rating, int countNumber) {
		this.rating = rating;
		this.countNumber = countNumber;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(int countNumber) {
		this.countNumber = countNumber;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel destination, int flags) {
		destination.writeDouble(rating);
		destination.writeInt(countNumber);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@SuppressWarnings("rawtypes")
		@Override
		public RatingResultItem createFromParcel(Parcel in) {
			return new RatingResultItem(in);
		}

		@Override
		public RatingResultItem[] newArray(int size) {
			return new RatingResultItem[size];
		}
	};

}
