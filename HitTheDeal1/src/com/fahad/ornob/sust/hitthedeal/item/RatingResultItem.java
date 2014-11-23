package com.fahad.ornob.sust.hitthedeal.item;

public class RatingResultItem {

	double rating;
	int countNumber;
	public RatingResultItem(double rating, int countNumber) {
		super();
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
	
	
}
