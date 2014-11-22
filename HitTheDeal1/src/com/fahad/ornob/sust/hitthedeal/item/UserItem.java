package com.fahad.ornob.sust.hitthedeal.item;

public class UserItem {

	int user_id, user_type_id;
	String user_name, address, email, phn_no; 
	int date_of_creation;
	double latitude, longitude;
	String image_url, password;
	int creator_type_id;
	
	
	
	
	public UserItem(int user_id, String user_name, String image_url,
			int creator_type_id) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.image_url = image_url;
		this.creator_type_id = creator_type_id;
	}




	public UserItem(int user_id, int user_type_id, String user_name,
			String address, String email, String phn_no, int date_of_creation,
			double latitude, double longitude, String image_url,
			String password, int creator_type_id) {
		super();
		this.user_id = user_id;
		this.user_type_id = user_type_id;
		this.user_name = user_name;
		this.address = address;
		this.email = email;
		this.phn_no = phn_no;
		this.date_of_creation = date_of_creation;
		this.latitude = latitude;
		this.longitude = longitude;
		this.image_url = image_url;
		this.password = password;
		this.creator_type_id = creator_type_id;
	}




	public int getUser_id() {
		return user_id;
	}




	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}




	public int getUser_type_id() {
		return user_type_id;
	}




	public void setUser_type_id(int user_type_id) {
		this.user_type_id = user_type_id;
	}




	public String getUser_name() {
		return user_name;
	}




	public void setUser_name(String user_name) {
		this.user_name = user_name;
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




	public String getPhn_no() {
		return phn_no;
	}




	public void setPhn_no(String phn_no) {
		this.phn_no = phn_no;
	}




	public int getDate_of_creation() {
		return date_of_creation;
	}




	public void setDate_of_creation(int date_of_creation) {
		this.date_of_creation = date_of_creation;
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




	public String getImage_url() {
		return image_url;
	}




	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public int getCreator_type_id() {
		return creator_type_id;
	}




	public void setCreator_type_id(int creator_type_id) {
		this.creator_type_id = creator_type_id;
	}
	
	
	
}
