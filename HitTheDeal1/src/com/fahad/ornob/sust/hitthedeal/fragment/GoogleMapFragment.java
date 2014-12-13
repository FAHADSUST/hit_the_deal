package com.fahad.ornob.sust.hitthedeal.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.fahad.ornob.sust.hitthedeal.EventDetailActivity;
import com.fahad.ornob.sust.hitthedeal.LoginPage;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.ViwerActivity;
import com.fahad.ornob.sust.hitthedeal.adapter.MarkerInfoWindowAdapter;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.CombinedEventAndUser;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "WorldWriteableFiles" })
public class GoogleMapFragment extends Fragment {

	private static final String TAG = GoogleMapFragment.class.getSimpleName();
	public static Context context;
	static ConnectionDetector cd;

	boolean isGPSFixed = false, isEventNotifierOn = false;
	static TextView textView;
	static GoogleMap googleMap;
	public static ArrayList<Event> events;
	public static ArrayList<UserItem> userItems;
	public static ArrayList<Marker> markers;

	Button button;
	Intent eventNotifierServiceIntent;
	SharedPreferences sharedPreferences;
	Editor editor;
	SupportMapFragment mapFragment;
	Bundle mapBundle;
	FragmentManager fm;
	public static View view;
	static Activity activity;
	static GoogleMapFragment googleMapFragment;

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.map_fragment_layout, null);
		cd = new ConnectionDetector(getActivity());
		context = getActivity();

		userItems = new ArrayList<UserItem>();

		// eventNotifierServiceIntent = new Intent(getActivity(),
		// EventNotifierService.class);
		activity = getActivity();
		googleMapFragment = new GoogleMapFragment();
		sharedPreferences = getActivity().getSharedPreferences(
				Constants.MY_PREFS, Context.MODE_WORLD_WRITEABLE);
		editor = sharedPreferences.edit();
		if (!sharedPreferences.contains(Constants.SERVICE_STATUS)) {
			editor.putBoolean(Constants.SERVICE_STATUS, false);
			editor.commit();
		}

		initilizeMap();

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

		return view;
	}

	private void initilizeMap() {
		/*
		 * if (googleMap != null) {
		 */
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
						/*Toast.makeText(
								getActivity(),
								events.get(markers.indexOf(marker))
										.getEventDescription(),
								Toast.LENGTH_SHORT).show();*/
						
						Event eventItem = events.get(markers.indexOf(marker));


					    Intent intent = new Intent(getActivity(),EventDetailActivity.class);
					    intent.putExtra("selectedEventId", eventItem.getEventId());
					    startActivity(intent);

					}
				});

		googleMap
				.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
					@Override
					public boolean onMarkerClick(
							com.google.android.gms.maps.model.Marker marker) {
						marker.showInfoWindow();
						return true;
					}
				});

		/*
		 * }else Toast.makeText(getActivity(), "Unable to create Maps",
		 * Toast.LENGTH_SHORT).show();
		 */
		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getActivity(), "Sorry! unable to create maps",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void makeVolleyRequest() {

		String url = Constants.urlGetEventAround + "longitude="
				+ Double.toString(ViwerActivity.currentLocation.getLongitude())
				+ "&latitude="
				+ Double.toString(ViwerActivity.currentLocation.getLatitude())
				+ "&distance=" + Constants.Distance;
		jsonUniAsync(url, 1);

	}

	public static void jsonUniAsync(String url, final int itemType) {

		if (!cd.isConnectingToInternet()) {
			Cache cache = AppController.getInstance().getRequestQueue()
					.getCache();
			Entry entry = cache.get(url);
			if (entry != null) {
				// fetch the data from cache
				try {
					String data = new String(entry.data, "UTF-8");
					try {
						parseJsonFeed(new JSONObject(data), itemType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		} else {
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET, url,
					null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response, itemType);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		}

	}

	private static void parseJsonFeed(JSONObject response, int itemType) {
		try {
			events.clear();
			int success = response.getInt(DataBaseKeys.Success);
			if (success == 1) {
				JSONArray eventsJsonArray = response.getJSONArray("all");

				for (int i = 0; i < eventsJsonArray.length(); i++) {
					JSONObject eventJsonObject = (JSONObject) eventsJsonArray
							.get(i);

					Event event = new Event(
							eventJsonObject.getInt(DataBaseKeys.EVENT_ID),
							eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
							eventJsonObject.getString(DataBaseKeys.EVENT_NAME),
							eventJsonObject
									.getString(DataBaseKeys.EVENT_DESCRIPTIOPN),

							eventJsonObject.getLong(DataBaseKeys.START_DATE),
							eventJsonObject.getLong(DataBaseKeys.END_DATE),
							eventJsonObject.getDouble(DataBaseKeys.LATITUDE),
							eventJsonObject.getDouble(DataBaseKeys.LONGITUDE),
							eventJsonObject.getString(DataBaseKeys.EVENT_IMG),
							eventJsonObject.getString(DataBaseKeys.EVENT_URL));
					events.add(event);

					UserItem userItem = new UserItem(
							eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
							eventJsonObject.getString(DataBaseKeys.USER_NAME),
							eventJsonObject
									.getString(DataBaseKeys.USER_IMAGE_URL),
							eventJsonObject
									.getInt(DataBaseKeys.USER_CREATOR_TYPE_ID));
					userItems.add(userItem);
				}

			} else {
				//Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
		markTheEventsOnMAp();
	}

	public static void createCirleOnMap() {
		if (ViwerActivity.currentLocation != null) {
			CircleOptions circleOptions = new CircleOptions()
					.center(new LatLng(ViwerActivity.currentLocation
							.getLatitude(), ViwerActivity.currentLocation
							.getLongitude())).radius(Constants.Distance*1000)
					.fillColor(Color.parseColor("#26EE1414")).strokeWidth(0);
			googleMap.addCircle(circleOptions);
			googleMap
					.animateCamera(CameraUpdateFactory.newLatLngZoom(
							(new LatLng(ViwerActivity.currentLocation
									.getLatitude(),
									ViwerActivity.currentLocation
											.getLongitude())), 13));
		} else {
			Toast.makeText(activity, "location null", Toast.LENGTH_LONG).show();
		}
	}

	private static HashMap<Marker, CombinedEventAndUser> mMarkersHashMap;

	static void markTheEventsOnMAp() {
		googleMap.clear();
		markers.clear();
		createCirleOnMap();
		int i = 0;
		mMarkersHashMap = new HashMap<Marker, CombinedEventAndUser>();
		for (Event event : events) {
			int type = userItems.get(i).getCreator_type_id() - 1;

			MarkerOptions markerOption = new MarkerOptions().position(
					new LatLng(event.getLatitude(), event.getLongitude()))
					.icon(BitmapDescriptorFactory
							.fromResource(Constants.iconMarkerType[type]));

			Marker marker = googleMap.addMarker(markerOption);
			CombinedEventAndUser combinedEventAndUser = new CombinedEventAndUser(
					userItems.get(i), event);
			mMarkersHashMap.put(marker, combinedEventAndUser);

			markers.add(marker);

			googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(context,
					mMarkersHashMap));

			i++;
		}

	}

}
