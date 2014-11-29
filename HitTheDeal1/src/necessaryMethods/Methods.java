package necessaryMethods;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.widget.Toast;
import constants.DBKeys;
import databaseEntities.Event;

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
						eventJsonObject.getInt(DBKeys.EVENT_ID),
						eventJsonObject.getInt(DBKeys.CREATOR_ID),
						eventJsonObject
								.getString(DBKeys.EVENT_DESCRIPTIOPN),
						eventJsonObject.getLong(DBKeys.START_DATE),
						eventJsonObject.getLong(DBKeys.END_DATE),
						eventJsonObject.getDouble(DBKeys.LATITUDE),
						eventJsonObject.getDouble(DBKeys.LONGITUDE),
						eventJsonObject.getString(DBKeys.EVENT_NAME),
						eventJsonObject.getString(DBKeys.EVENT_IMG),
						eventJsonObject.getString(DBKeys.EVENT_URL));
				events.add(event);
			} catch (JSONException e) {
				// Methods.makeToast(EventNotifierService.this, e.toString(),
				// Toast.LENGTH_LONG);
			}
		}
		return events;
	}
}
