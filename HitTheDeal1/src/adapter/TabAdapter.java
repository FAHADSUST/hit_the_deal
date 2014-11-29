package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import fragments.GoogleMapFragment;

public class TabAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new GoogleMapFragment();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position) {
		case 0:
			title = "Map";
			break;
		}
		return title;
	}

}
