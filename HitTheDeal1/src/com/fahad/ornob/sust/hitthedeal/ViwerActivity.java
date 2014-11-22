package com.fahad.ornob.sust.hitthedeal;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.fahad.ornob.sust.hitthedeal.adapter.TabsPagerAdapter;
import com.fahad.ornob.sust.hitthedeal.fragment.GoogleMapFragment;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.google.android.gms.maps.model.Marker;



import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.app.Service;

import android.location.Location;
import android.location.LocationListener;


@SuppressLint("NewApi")
public class ViwerActivity extends FragmentActivity implements
		ActionBar.TabListener , LocationListener{

	LocationManager locationManager;
	boolean isGPSFixed = false;
	Timer timer = null;
	TimerTask timerTask;
	public static Location currentLocation = null;
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Map", "Around", "All","Fav","Profile" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		GoogleMapFragment.events = new ArrayList<Event>();	
		GoogleMapFragment.markers = new ArrayList<Marker>();
		createLocationManager();
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(5);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	////Ornob ko//
	private void createLocationManager() {
		locationManager = (LocationManager) this
				.getSystemService(Service.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 5, 0, ViwerActivity.this);
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
				ViwerActivity.this.runOnUiThread(new Runnable() {

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
