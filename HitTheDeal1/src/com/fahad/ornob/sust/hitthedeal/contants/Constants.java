package com.fahad.ornob.sust.hitthedeal.contants;

import android.media.RingtoneManager;
import android.net.Uri;

public class Constants {
	public final static String URL = "http://192.168.207.1:8080/Android_Database_Connector/";
	public final static String MY_PREFS = "MyPrefs";
	public final static String SERVICE_STATUS = "isServiceOn";
	public final static int NOTIFICATION_ID = 100;
	public final static Uri NOTIFICATION_SOUND = RingtoneManager
			.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	
	
	public static final int Show = 0;
	public static final int Hide = 1;
	public static final int Creator=1;
	public static final int Visitor=2;
	
	public final static String urlLogin = "http://192.168.2.107:8084/AndroidLogin/LoginHitTheDeal?";//localhost
	public static String warnUserName;
	public static String warnTitle="Error!";
	public static String warnEmail="Email can nto be Empty.";
	public static String warnPass="Password can nto be Empty.";
	public static String warnEmailPatern="Invalid Email.";

	
}
