package com.fahad.ornob.sust.hitthedeal.item;

public class CombinedEventAndUser {

	public UserItem userItem;
	public Event event;
	public CombinedEventAndUser(UserItem userItem, Event event) {
		super();
		this.userItem = userItem;
		this.event = event;
	}
	public UserItem getUserItem() {
		return userItem;
	}
	public void setUserItem(UserItem userItem) {
		this.userItem = userItem;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
}
