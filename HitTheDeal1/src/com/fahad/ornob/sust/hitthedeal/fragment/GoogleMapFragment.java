package com.fahad.ornob.sust.hitthedeal.fragment;

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
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.necessary_method.Methods;
import com.fahad.ornob.sust.hitthedeal.service.EventNotifierService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "WorldWriteableFiles" })
public class GoogleMapFragment extends Fragment implements LocationListener {

	public static Timer timer = null;
	private static TimerTask timerTask;
	ProgressBar progressBar;
	public Location currentLocation = null;
	boolean isGPSFixed = false, isEventNotifierOn = false;
	TextView textView;
	GoogleMap googleMap;
	LocationManager locationManager;
	ArrayList<Event> events;
	ArrayList<Marker> markers;
	FrameLayout activityLayout;
	Button button;
	Intent eventNotifierServiceIntent;
	SharedPreferences sharedPreferences;
	Editor editor;
	static View view;
	SupportMapFragment mapFragment;
	Bundle mapBundle;
	FragmentManager fm;

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
			view = inflater.inflate(
					R.layout.map_fragment_layout, null);

			sharedPreferences = getActivity().getSharedPreferences(
					Constants.MY_PREFS, Context.MODE_WORLD_WRITEABLE);
			editor = sharedPreferences.edit();
			if (!sharedPreferences.contains(Constants.SERVICE_STATUS)) {
				editor.putBoolean(Constants.SERVICE_STATUS, false);
				editor.commit();
			}

			events = new ArrayList<Event>();
			markers = new ArrayList<Marker>();

			eventNotifierServiceIntent = new Intent(getActivity(),
					EventNotifierService.class);
			progressBar = (ProgressBar) view.findViewById(R.id.progress_circular);

			// button = (Button) view.findViewById(R.id.bt);
			// button.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// if (!sharedPreferences.getBoolean(Constants.SERVICE_STATUS,
			// false)) {
			// Intent bundle = new Intent();
			// getActivity().startService(eventNotifierServiceIntent);
			// editor.putBoolean(Constants.SERVICE_STATUS, true);
			// editor.commit();
			// isEventNotifierOn = true;
			// } else {
			// getActivity().stopService(eventNotifierServiceIntent);
			// editor.putBoolean(Constants.SERVICE_STATUS, false);
			// editor.commit();
			// isEventNotifierOn = false;
			// }
			// }
			// });

			initilizeMap();
			createLocationManager();
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    }
		


		return view;
	}

	private void createLocationManager() {
		locationManager = (LocationManager) getActivity().getSystemService(
				Service.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 5, 0, GoogleMapFragment.this);
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		new GPSFixer().execute();
	}

	private void initilizeMap() {
		// mapFragment = ((SupportMapFragment) getActivity()
		// .getSupportFragmentManager().findFragmentById(R.id.map));
		googleMap = ((SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap
				.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

					@Override
					public void onMyLocationChange(Location location) {

					}
				});
		googleMap
				.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						Toast.makeText(
								getActivity(),
								events.get(markers.indexOf(marker))
										.getEventDescription(),
								Toast.LENGTH_SHORT).show();
					}
				});

		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getActivity(), "Sorry! unable to create maps",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void makeVolleyRequest() {

		StringRequest strReq = new StringRequest(Method.POST,
				Constants.URL + "AndroidConnector",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray eventsWithInTheRange = new JSONArray(
										response);
								JSONObject successObject = (JSONObject) eventsWithInTheRange
										.get(eventsWithInTheRange.length() - 1);
								if (successObject.getBoolean("success") == false) {
									Toast.makeText(getActivity(),
											"Database Connection Failed",
											Toast.LENGTH_LONG).show();
								} else {
									jsonToArrayLiast(eventsWithInTheRange);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							textView.setText("null");
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Methods.makeToast(getActivity(), "Network Error",
								Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("latitude",
						Double.toString(currentLocation.getLatitude()));
				params.put("longitude",
						Double.toString(currentLocation.getLongitude()));

				return params;
			}

		};

		Volley.newRequestQueue(getActivity()).add(strReq);
	}

	void jsonToArrayLiast(JSONArray eventsJsonArray) {
		events.clear();
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
				// Methods.makeToast(EventNotifierService.this, e.toString(),
				// Toast.LENGTH_LONG);
			}
		}
		googleMap.clear();
		markTheEventsOnMAp();
	}

	void createCirleOnMap() {
		if (currentLocation != null) {
			CircleOptions circleOptions = new CircleOptions()
					.center(new LatLng(currentLocation.getLatitude(),
							currentLocation.getLongitude())).radius(2000)
					.fillColor(Color.parseColor("#26EE1414")).strokeWidth(0);
			googleMap.addCircle(circleOptions);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					(new LatLng(currentLocation.getLatitude(), currentLocation
							.getLongitude())), 13));
		} else {
			Methods.makeToast(getActivity(), "location null", Toast.LENGTH_LONG);
		}
	}

	void markTheEventsOnMAp() {
		markers.clear();
		for (Event event : events) {
			Marker marker = googleMap.addMarker(new MarkerOptions().position(
					new LatLng(event.getLatitude(), event.getLongitude()))
					.title(event.getEventDescription()));
			markers.add(marker);
		}
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
			progressBar.setVisibility(View.INVISIBLE);
			startTimer();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		isGPSFixed = true;
		currentLocation = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	void startTimer() {
		timer = new Timer();
		initializeTimerTask();
		timer.scheduleAtFixedRate(timerTask, 0, 30000);
	}

	public static void cancelTimer() {
		if (timer != null && timerTask != null) {
			timerTask.cancel();
			timer.purge();
			timer.cancel();
		}
	}

	void initializeTimerTask() {
		timerTask = new TimerTask() {

			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						createCirleOnMap();
						makeVolleyRequest();
					}
				});
			}
		};
	}
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    if (googleMap != null) {
	        getFragmentManager()
	                .beginTransaction()
	                .remove(getFragmentManager().findFragmentById(R.id.map))
	                .commit();
	    }
	}
}
