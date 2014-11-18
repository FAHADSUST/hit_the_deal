package com.fahad.ornob.sust.hitthedeal.item;

public class CreatorTypeItem {

	int creator_type_id;
	String creator_type_name;
	public CreatorTypeItem(int creator_type_id, String creator_type_name) {
		super();
		this.creator_type_id = creator_type_id;
		this.creator_type_name = creator_type_name;
	}
	
	public int getCreator_type_id() {
		return creator_type_id;
	}
	public void setCreator_type_id(int creator_type_id) {
		this.creator_type_id = creator_type_id;
	}
	public String getCreator_type_name() {
		return creator_type_name;
	}
	public void setCreator_type_name(String creator_type_name) {
		this.creator_type_name = creator_type_name;
	}
	
	
}
