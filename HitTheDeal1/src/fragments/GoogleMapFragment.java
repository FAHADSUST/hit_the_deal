package fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import necessaryMethods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.EventNotifierService;
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

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fahad.ornob.sust.hitthedeal.MainActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import constants.Constants;
import constants.DBKeys;
import databaseEntities.Event;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "WorldWriteableFiles" })
public class GoogleMapFragment extends Fragment {

	boolean isGPSFixed = false, isEventNotifierOn = false;
	static TextView textView;
	static GoogleMap googleMap;
	public static ArrayList<Event> events;
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
	int countCall = 0;

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(
				R.layout.map_fragment_layout, null);

		eventNotifierServiceIntent = new Intent(getActivity(),
				EventNotifierService.class);
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

		return view;
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

	public static void makeVolleyRequest() {

		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "AndroidConnector",
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
									Toast.makeText(activity,
											"Database Connection Failed",
											Toast.LENGTH_LONG).show();
								} else {
									googleMapFragment
											.jsonToArrayLiast(eventsWithInTheRange);
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
						Methods.makeToast(activity, "Network Error",
								Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("latitude", Double
						.toString(MainActivityOrnob.currentLocation.getLatitude()));
				params.put("longitude", Double
						.toString(MainActivityOrnob.currentLocation.getLongitude()));

				return params;
			}

		};

		Volley.newRequestQueue(activity).add(strReq);
	}

	void jsonToArrayLiast(JSONArray eventsJsonArray) {
		events.clear();
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
		markTheEventsOnMAp();
	}

	public static void createCirleOnMap() {
		if (MainActivityOrnob.currentLocation != null) {
			CircleOptions circleOptions = new CircleOptions()
					.center(new LatLng(MainActivityOrnob.currentLocation
							.getLatitude(), MainActivityOrnob.currentLocation
							.getLongitude())).radius(2000)
					.fillColor(Color.parseColor("#26EE1414")).strokeWidth(0);
			googleMap.addCircle(circleOptions);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					(new LatLng(MainActivityOrnob.currentLocation.getLatitude(),
							MainActivityOrnob.currentLocation.getLongitude())),
					13.5f));
		} else {
			Methods.makeToast(activity, "location null", Toast.LENGTH_LONG);
		}
	}

	void markTheEventsOnMAp() {
		googleMap.clear();
		markers.clear();
		createCirleOnMap();
		for (Event event : events) {
			Marker marker = googleMap.addMarker(new MarkerOptions().position(
					new LatLng(event.getLatitude(), event.getLongitude()))
					.title(event.getEventDescription()));
			markers.add(marker);
		}
	}

}
