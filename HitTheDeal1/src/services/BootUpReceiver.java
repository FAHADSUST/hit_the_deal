package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import constants.Constants;

public class BootUpReceiver extends BroadcastReceiver {

	SharedPreferences sharedPreferences;
	Editor editor;
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		
		if(getValueSharedBooleanPref(com.fahad.ornob.sust.hitthedeal.contants.Constants.KeyServiceOnOff)){
			sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS,
					Context.MODE_WORLD_WRITEABLE);
			editor = sharedPreferences.edit();
			editor.putBoolean(Constants.SERVICE_STATUS, true);
			editor.commit();
			context.startService(new Intent(context, EventNotifierService.class));
		}
	}
	
	public  boolean getValueSharedBooleanPref(String key){
		SharedPreferences prefs = context.getSharedPreferences("my_fref", context.MODE_PRIVATE); 
		boolean restoredText = prefs.getBoolean(key, true);
		
		return restoredText;
	}

}
