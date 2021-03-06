package com.fahad.ornob.sust.hitthedeal.adapter;


import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.fragment.AroundMeFragment;
import com.fahad.ornob.sust.hitthedeal.fragment.GoogleMapFragment;
import com.fahad.ornob.sust.hitthedeal.fragment.AllEventsFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewerProfileTabPagerAdapter extends FragmentPagerAdapter {

	public ViewerProfileTabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new GoogleMapFragment();//TopRatedFragment();
		case 1:
			// Games fragment activity
			return new AroundMeFragment();
		
			
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
