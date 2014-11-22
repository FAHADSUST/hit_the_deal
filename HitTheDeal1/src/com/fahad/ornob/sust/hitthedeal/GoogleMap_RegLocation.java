package com.fahad.ornob.sust.hitthedeal;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GoogleMap_RegLocation extends Activity implements
		OnMapClickListener, OnMapLongClickListener {

	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;

	Location myLocation;
	Button closeMapB;
	Marker marker = null;

	Double donor_latitude;
	Double donor_longitude;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment_layout);

		closeMapB = (Button) findViewById(R.id.closeB);

		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment) myFragmentManager
				.findFragmentById(R.id.mapCreatorSignUp);
		myMap = myMapFragment.getMap();

		myMap.setMyLocationEnabled(true);

		// myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		myLocation();
		myMap.setOnMapClickListener(this);
		myMap.setOnMapLongClickListener(this);

	}

	private void myLocation() {
		// TODO Auto-generated method stub

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		String provider = locationManager.getBestProvider(criteria, true);
		Location myLocation = locationManager.getLastKnownLocation(provider);

		double latitude = myLocation.getLatitude();
		double longitude = myLocation.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		MarkerOptions marker = new MarkerOptions().position(latLng).title(
				"My Location!");
		myMap.addMarker(marker);

		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {
			Toast.makeText(getApplicationContext(),
					"isGooglePlayServicesAvailable SUCCESS", Toast.LENGTH_LONG)
					.show();
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					RQS_GooglePlayServices);
		}

	}

	@Override
	public void onMapClick(LatLng point) {		
		myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
	}

	@Override
	public void onMapLongClick(LatLng point) {
		
		if (marker != null) {
			marker.remove();
		}
		marker = myMap.addMarker(new MarkerOptions().position(point)
				.draggable(true).visible(true));

		donor_latitude = point.latitude;
		donor_longitude = point.longitude;

		new AlertDialog.Builder(GoogleMap_RegLocation.this)
				.setTitle("Setting Position")
				.setMessage(
						"Are you sure you want to set this location as your fixed position ?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing

								marker.remove();
							}
						}).show();

	}

}