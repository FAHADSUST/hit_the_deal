package databaseEntities;

public class FeedbackMaker {
	int userId;
	String userName, imageUrl;

	public FeedbackMaker(int userId, String userName, String imageUrl) {
		this.userId = userId;
		this.userName = userName;
		this.imageUrl = imageUrl;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrlString) {
		this.imageUrl = imageUrlString;
	}

}
