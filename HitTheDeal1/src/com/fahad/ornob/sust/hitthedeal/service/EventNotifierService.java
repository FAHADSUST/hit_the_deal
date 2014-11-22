package com.fahad.ornob.sust.hitthedeal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.ViwerActivity;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.necessary_method.Methods;



@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class EventNotifierService extends Service implements LocationListener {

	LocationManager locationManager;
	Location currentLocation;
	boolean isGPSFixed;
	Timer timer;
	TimerTask timerTask;
	SharedPreferences sharedPreferences;
	Editor editor;
	ArrayList<Event> eventsWithInRange;
	JSONArray eventsJsonArray = null;
	NotificationManager notificationManager;
	int notificationCount = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		eventsWithInRange = new ArrayList<Event>();
		eventsWithInRange.clear();
		sharedPreferences = getSharedPreferences(Constants.MY_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		editor = sharedPreferences.edit();
		editor.putBoolean(Constants.SERVICE_STATUS, true);
		Methods.makeToast(EventNotifierService.this, "Service Started",
				Toast.LENGTH_SHORT);
		createLocationManager();
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		editor.putBoolean(Constants.SERVICE_STATUS, true);
		timer.cancel();
		Methods.makeToast(EventNotifierService.this, "Service Stoppd",
				Toast.LENGTH_SHORT);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void createLocationManager() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0,
				EventNotifierService.this);
		new GPSFixer().execute();
	}

	class GPSFixer extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			while (!isGPSFixed) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			startTimer();
		}
	}

	void startTimer() {
		timer = new Timer();
		initializeTimerTask();
		timer.scheduleAtFixedRate(timerTask, 0, 300000);
	}

	void initializeTimerTask() {
		timerTask = new TimerTask() {

			@Override
			public void run() {
				makeVolleyRequest();
			}
		};
	}

	void makeVolleyRequest() {

		JsonObjectRequest strReq = new JsonObjectRequest(Method.GET,
				Constants.urlCommon+"GetVisitorAroundEvent",null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (response != null) {
							try {
								int success = response.getInt(DataBaseKeys.Success);
								if(success==1){
									JSONArray eventsJsonArray = response.getJSONArray("all");
									
									notifyUser(Methods
												.eventJsonArrayToList(eventsJsonArray));
	
									
								}else{
									Toast.makeText(EventNotifierService.this, "Fail", Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						} else {

						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Methods.makeToast(EventNotifierService.this,
								"Network Error", Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("longitude",
						Double.toString(currentLocation.getLongitude()));
				params.put("latitude",
						Double.toString(currentLocation.getLatitude()));
				params.put("distance",
						Double.toString(Constants.Distance));
				

				return params;
			}

		};

		Volley.newRequestQueue(getApplicationContext()).add(strReq);
	}

	void jsonToArrayLiast(JSONArray eventsJsonArray) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (int i = 0; i < eventsJsonArray.length() - 1; i++) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				Event event = new Event(
						eventJsonObject.getInt(DataBaseKeys.EVENT_ID),
						eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
						eventJsonObject
								.getString(DataBaseKeys.EVENT_DESCRIPTIOPN),
						eventJsonObject.getLong(DataBaseKeys.START_DATE),
						eventJsonObject.getLong(DataBaseKeys.END_DATE),
						eventJsonObject.getDouble(DataBaseKeys.LATITUDE),
						eventJsonObject.getDouble(DataBaseKeys.LONGITUDE));
				events.add(event);
			} catch (JSONException e) {
				Methods.makeToast(EventNotifierService.this, e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		notifyUser(events);
	}

	void notifyUser(ArrayList<Event> events) {
		// Methods.makeToast(EventNotifierService.this, eventsWithInRange.size()
		// + "", Toast.LENGTH_SHORT);
		if (eventsWithInRange != null) {
			for (int i = 0; i < events.size(); i++) {
				Event event = events.get(i);
				boolean flag = false;
				for (int j = 0; j < eventsWithInRange.size(); j++) {
					if (event.getEventId() == eventsWithInRange.get(j)
							.getEventId()) {
						flag = true;
					}
				}
				if (!flag) {
					fireNotification(event);
				}
			}
			eventsWithInRange.clear();
			eventsWithInRange = new ArrayList<Event>(events);

		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	void fireNotification(Event event) {
		notificationCount++;
		Intent intent = new Intent(this, ViwerActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification notification = new Notification.Builder(this)
				.setContentTitle(event.getEventDescription())
				.setContentText(event.getEventDescription())
				.setSound(Constants.NOTIFICATION_SOUND)
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.addAction(R.drawable.ic_launcher, "Call", pIntent)
				.addAction(R.drawable.ic_launcher, "More", pIntent)
				.addAction(R.drawable.ic_launcher, "And more", pIntent).build();

		// notification.flags = Notification.PRIORITY_HIGH;
		notificationManager.notify(notificationCount, notification);
	}

	@Override
	public void onLocationChanged(Location location) {
		isGPSFixed = true;
		currentLocation = location;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
}
