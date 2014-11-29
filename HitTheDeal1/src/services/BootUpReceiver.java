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

	@Override
	public void onReceive(Context context, Intent intent) {
		sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		editor = sharedPreferences.edit();
		editor.putBoolean(Constants.SERVICE_STATUS, true);
		editor.commit();
		context.startService(new Intent(context, EventNotifierService.class));
	}

}
