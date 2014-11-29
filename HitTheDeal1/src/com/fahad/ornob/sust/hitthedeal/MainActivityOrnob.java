package com.fahad.ornob.sust.hitthedeal;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import services.EventNotifierService;

import adapter.TabAdapter;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;


import com.fahad.ornob.sust.hitthedeal.R;
import com.google.android.gms.maps.model.Marker;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import constants.Constants;

import databaseEntities.Event;
import fragments.GoogleMapFragment;

public class MainActivityOrnob extends FragmentActivity implements LocationListener {

	LocationManager locationManager;
	TabAdapter tabAdapter;
	ViewPager viewPager;
	PageIndicator pageIndicator;
	public static FragmentManager fragmentManager = null;
	boolean isGPSFixed = false;
	Timer timer = null;
	TimerTask timerTask;
	public static Location currentLocation = null;
	SharedPreferences sharedPreferences;
	Editor editor;
	Intent eventNotifierServiceIntent;
	boolean isEventNotifierOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GoogleMapFragment.events = new ArrayList<Event>();
		GoogleMapFragment.markers = new ArrayList<Marker>();
		createLocationManager();
		createFragments();

		sharedPreferences = getSharedPreferences(
				Constants.MY_PREFS, Context.MODE_WORLD_WRITEABLE);
		eventNotifierServiceIntent = new Intent(MainActivityOrnob.this,
				EventNotifierService.class);
		
		 if (!sharedPreferences.getBoolean(Constants.SERVICE_STATUS,
		 false)) {
		 Intent bundle = new Intent();
		 startService(eventNotifierServiceIntent);
		 editor.putBoolean(Constants.SERVICE_STATUS, true);
		 editor.commit();
		 isEventNotifierOn = true;
		 } else {
		 stopService(eventNotifierServiceIntent);
		 editor.putBoolean(Constants.SERVICE_STATUS, false);
		 editor.commit();
		 isEventNotifierOn = false;
		 }
		
	}

	void createFragments() {
		fragmentManager = getSupportFragmentManager();
		tabAdapter = new TabAdapter(getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(7);
		viewPager.setAdapter(tabAdapter);

		pageIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pageIndicator.setViewPager(viewPager);
	}

	private void createLocationManager() {
		locationManager = (LocationManager) MainActivityOrnob.this
				.getSystemService(Service.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 5, 0, MainActivityOrnob.this);
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
		timer.scheduleAtFixedRate(timerTask, 0, 30000);
	}

	void cancelTimer() {
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
				MainActivityOrnob.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						GoogleMapFragment.createCirleOnMap();
						GoogleMapFragment.makeVolleyRequest();
					}
				});
			}
		};
	}

	@Override
	protected void onDestroy() {
		cancelTimer();
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		isGPSFixed = true;
		currentLocation = location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}