package com.fahad.ornob.sust.hitthedeal;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;


import com.fahad.ornob.sust.hitthedeal.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import databaseEntities.Event;

public class EventFromNotificationActivityOrnob extends Activity {

	GoogleMap googleMap;
	Event event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_from_noitification);

		event = getIntent().getExtras().getParcelable("event");

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map10)).getMap();

			// check if map is created successfully or not
			if (event != null) {
				String markerTitle = event.getEventName() + "\n\n"
						+ event.getEventDescription() + "\n\n"
						+ setTimeTextView(event.getStartDate()) + "-"
						+ setTimeTextView(event.getEndDate());

				googleMap.addMarker(new MarkerOptions().position(
						new LatLng(event.getLatitude(), event.getLongitude()))
						.title(markerTitle));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						(new LatLng(event.getLatitude(),
								event.getLongitude())),
						13.5f));
			}
		}
	}

	String setTimeTextView(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault());
		int year = calendar.get(Calendar.YEAR);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		String message = day + "-" + month + "-" + year + "  " + hours + ":"
				+ minutes;
		return message;
	}
}
