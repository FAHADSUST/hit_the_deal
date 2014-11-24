package com.fahad.ornob.sust.hitthedeal.contants;

public class DataBaseKeys {
	public final static String EVENT_ID = "event_id";
	public final static String CREATOR_ID = "creator_id";
	public final static String EVENT_NAME = "event_name";
	public final static String EVENT_DESCRIPTIOPN = "event_description";
	public final static String START_DATE = "start_date";
	public final static String END_DATE = "end_date";
	public final static String LONGITUDE = "longitude";
	public final static String LATITUDE = "latitude";
	public final static String EVENT_IMG="event_img";
	public final static String EVENT_URL="event_url";
	
	public final static String user_type_id="user_type_id";
	
	public final static String Success = "success";
	
	public final static String creatorTypekey[] = {"creator_type_id", "creator_type_name"};
	
	public final static String USER_NAME = "user_name";
	public final static String USER_IMAGE_URL = "image_url";
	public final static String USER_CREATOR_TYPE_ID = "creator_type_id";
	public static final String EventDetaiTag = "eventDetail";
	public static final String RatingDetailTag = "ratingDetail";
	
	public static final String RatingCounNumberTag="countNumber";
	public static final String RatingTag = "rating";
	
	public static final String  FeedBackJsonObjTag="feedDetail";
	public static final String  FeedBackJsonArrayTag="allFeedBack";
	public static final String  FeedBackTag[]={"feedback_id", "event_id", "viewer_id", "feedback", "date"};
	
	public static final int GetEventDetailType = 1;
	public static final int InsertRatingType = 2;
	public static final int InserFeedBackType = 3;
	
	public static final String  UserDataTAG="userData";
	public static final String  userKey[] = {"user_id", "user_type_id", "user_name", "address", "email", "phn_no", "date_of_creation", "latitude", "longitude", "image_url", "password", "creator_type_id"};
	
	public static final String  MYViwerRatingTag="myViwerRating";
	
}
