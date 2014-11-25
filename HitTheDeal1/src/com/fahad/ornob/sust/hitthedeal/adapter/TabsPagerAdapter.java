package com.fahad.ornob.sust.hitthedeal.adapter;


import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.fragment.AroundMeFragment;
import com.fahad.ornob.sust.hitthedeal.fragment.GoogleMapFragment;
import com.fahad.ornob.sust.hitthedeal.fragment.MoviesFragment;
import com.fahad.ornob.sust.hitthedeal.fragment.MyFavCreatorFragment;
import com.fahad.ornob.sust.hitthedeal.fragment.ViwerProfileFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
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
		case 2:
			// Movies fragment activity
			return new MyFavCreatorFragment();
		case 3:
			// Movies fragment activity
			return new ViwerProfileFragment();
			
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
