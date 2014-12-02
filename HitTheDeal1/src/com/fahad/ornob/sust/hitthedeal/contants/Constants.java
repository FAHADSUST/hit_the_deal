package com.fahad.ornob.sust.hitthedeal.contants;

import java.io.File;

import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;

public class Constants {
	
	public static final File mainFolderpath = new File(Environment.getExternalStorageDirectory(),
			"Image keeper");
	
	public static UserItem userItem=null;
	
	//public final static String URL = "http://192.168.2.105:8084/AndroidLogin/";
	public final static String MY_PREFS = "MyPrefs";
	public final static String SERVICE_STATUS = "isServiceOn";
	public final static int NOTIFICATION_ID = 100;
	public final static Uri NOTIFICATION_SOUND = RingtoneManager
			.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	
	
	public static final int ShowLogin = 1;
	public static final int ShowCreatorSignUp = 2;
	public static final int ShowVisitorSignUp = 3;
	public static final int Creator=1;
	public static final int Visitor=2;
	
	public final static String urlCommon="http://192.168.2.104:8084/AndroidLogin/";
	public final static String urlLogin = urlCommon+"LoginHitTheDeal?";//localhost
	public final static String urlCreatorType = urlCommon+"GetCreatorType";	
	public final static String urlInsertSignUpData=urlCommon+"SignUp";
	public final static String urlGetEventAround=urlCommon+"GetVisitorAroundEvent?";
	public final static String urlGetAllEvent = urlCommon+"GetAllEvent";
	public final static String urlInserRating =urlCommon+"InsertEventRating?";
	public final static String urlGetSelectedEventDetail =urlCommon+"GetSelectedEventDetail?";
	public final static String urlInsertFeedBack=urlCommon+"InsertEventFeedBack";
	public final static String urlGetMyFavCreator=urlCommon+"GetMyFavCreators?";
	public final static String urlGetEventFeedBack=urlCommon+"GetEventFeedback?";
	
	
	public final static String urlFileUploadServlet=urlCommon+"ImageUploader";
	public final static String urlgetImgServlet=urlCommon+"profile_images/";
	
	
	public static String warnUserName;
	public static String warnTitle="Error!";
	public static String warnEmail="Email can't be Empty.";
	public static String warnPass="Password can't be Empty.";
	public static String warnEmailPatern="Invalid Email.";
	
	public static String warnOrgName="Organization Name Can't be Empty.";
	public static String warnAddess="Address can't be Empty.";
	public static String warnLocation="You have to Pick a location of your Organization.";

	public static final int loginType=1;
	public static final int CreatorTypeItem=2;
	public static final int CreatorSignUp=3;
	public static final int ViwerSignUp=4;
	
	
	public static final int MapViewGone=0;
	public static final int MapViewShow=1;
	
	
	
	public static final int RQS_GooglePlayServices = 1;

	public static final String ImgEmtyTAG = "Empty";
	
	public static String FolderName="Hit_the_deal";
	
	public static final int ViwerTypeID=2;
	public static final int CreatorTypeID=1;
	
	
	public static final int LoginActivityPage=0;
	public static final int ViwerActivityPage=2;
	public static final int CreatorActivityPage=1;
	public static final int ViwerSignUpPage=3;
	public static final int CreatorSignUpPage=4;

	public static final double Distance = 3;
	
	
	public static int iconMarkerType[]={R.drawable.icon_pin_local_buseness,R.drawable.icon_pin_restaurent,R.drawable.icon_pin_culter,R.drawable.icon_pin_cause,R.drawable.icon_pin_others,R.drawable.icon_pin_others};
	public static int iconMarkerShowInfoType[]={R.drawable.business_icon,R.drawable.restaurent_icon,R.drawable.culter_icon,R.drawable.cause_icon,R.drawable.other_icon,R.drawable.icon_pin_others};
	
	
	public static int iconMyLocationMarker = R.drawable.icon_my_location;

	
}
