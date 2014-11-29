package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;


import fragments.FragmentFavouriteCreatorEvents;
import fragments.FragmentFavourtieCreatorAbout;

public class FavouriteCreatorTabAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	public FavouriteCreatorTabAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		switch (position) {
		case 0:
			Fragment fragment1 = new FragmentFavourtieCreatorAbout();
			return fragment1;
		case 1:
			Fragment fragment2 = new FragmentFavouriteCreatorEvents();
			return fragment2;
		default:
			Fragment fragment0 = null;
			return fragment0;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position) {
		case 0:
			title = "About";
			break;
		case 1:
			title = "Events";
			break;
		}
		return title;
	}

}
