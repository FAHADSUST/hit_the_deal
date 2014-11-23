package com.fahad.ornob.sust.hitthedeal.necessary_method;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.Event;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.widget.Toast;


public class Methods {

	public static void makeToast(Context context, String message, int duration) {
		Toast.makeText(context, message, duration).show();
	}

	public static void fireNotification(
			NotificationManager notificationManager, Service service,
			Class sClass, String[] messages) {

	}

	public static ArrayList<Event> eventJsonArrayToList(
			JSONArray eventsJsonArray) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (int i = 0; i < eventsJsonArray.length() - 1; i++) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				Event event = new Event(
						eventJsonObject.getInt(DataBaseKeys.EVENT_ID),
						eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
						eventJsonObject
						.getString(DataBaseKeys.EVENT_NAME),
						eventJsonObject
								.getString(DataBaseKeys.EVENT_DESCRIPTIOPN),
						eventJsonObject.getLong(DataBaseKeys.START_DATE),
						eventJsonObject.getLong(DataBaseKeys.END_DATE),
						eventJsonObject.getDouble(DataBaseKeys.LATITUDE),
						eventJsonObject.getDouble(DataBaseKeys.LONGITUDE),
						eventJsonObject
						.getString(DataBaseKeys.EVENT_IMG),
						eventJsonObject
						.getString(DataBaseKeys.EVENT_URL));
						
				events.add(event);
			} catch (JSONException e) {
				// Methods.makeToast(EventNotifierService.this, e.toString(),
				// Toast.LENGTH_LONG);
			}
		}
		return events;
	}
}
