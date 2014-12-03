package constants;

import android.media.RingtoneManager;
import android.net.Uri;

public class Constants {
	public final static String URL = com.fahad.ornob.sust.hitthedeal.contants.Constants.urlCommon;
	//public final static String URL = "http://hitthedeal-fahadsust.rhcloud.com/hitthedeal/";
	
	//public final static String URL = "http://192.168.2.104:8084/AndroidLogin/";//"http://hitthedeal-fahadsust.rhcloud.com/hitthedeal/";////
	
	public final static String MY_PREFS = "MyPrefs";
	public final static String SERVICE_STATUS = "isServiceOn";
	public final static int NOTIFICATION_ID = 100;
	public final static Uri NOTIFICATION_SOUND = RingtoneManager
			.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

}
