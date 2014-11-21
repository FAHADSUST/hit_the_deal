package com.fahad.ornob.sust.hitthedeal.contants;

import java.io.File;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;

public class Constants {
	
	public static final File mainFolderpath = new File(Environment.getExternalStorageDirectory(),
			"Image keeper");
	
	public final static String URL = "http://192.168.207.1:8080/Android_Database_Connector/";
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
	
	public final static String urlLogin = "http://192.168.2.100:8084/AndroidLogin/LoginHitTheDeal?";//localhost
	public final static String urlCreatorType = "http://192.168.2.100:8084/AndroidLogin/GetCreatorType";
	public final static String urlFileUpload="http://192.168.2.100/db_project_uploaded_img/upload_image_swing.php";
	public final static String urlInsertSignUpData="http://192.168.2.100:8084/AndroidLogin/SignUp?";
	
	public final static String urlImg="http://192.168.2.100/db_project_uploaded_img/img/";
	
	
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
	
	
	public static final int MapViewGone=0;
	public static final int MapViewShow=1;
	
	
	
	public static final int RQS_GooglePlayServices = 1;

	public static final String ImgEmtyTAG = "Empty";
	
	public static String FolderName="Hit_the_deal";
	
}
