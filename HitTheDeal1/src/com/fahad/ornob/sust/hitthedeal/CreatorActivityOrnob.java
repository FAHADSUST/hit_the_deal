package com.fahad.ornob.sust.hitthedeal;

import adapter.CreatorTabAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.customImageView.CustomNetworkImageView;
import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import customviews.PagerSlidingTabStrip;
import databaseEntities.Creator1;

@SuppressLint({ "NewApi", "ResourceAsColor", "ClickableViewAccessibility" })
public class CreatorActivityOrnob extends FragmentActivity {

	FloatingActionButton floatingActionButton;
	boolean isDown = true;
	Animation slideUp, slideDown;
	Button myEventsButton, createEventButton;
	FrameLayout frameLayout;
	LinearLayout slidingLayout;
	NetworkImageView profileImageView;
	FrameLayout anchorLayout;
	public static Creator1 creator =null ;
	TextView nameTv, addressTv, phnTv, emailTv, typeTv;
	ViewPager viewPager;
	CreatorTabAdapter tabAdapter;
	PagerSlidingTabStrip tabs;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creator);

//		creator = new Creator1(4, "Abu Shahriar Ratul", "sylhet",
//				"ratul@gmail.com", "1234567", 1234, 24.8997635, 91.8619037,
//				"http://api.androidhive.info/feed/img/life.jpg", 2);
		
		creator = getIntent().getExtras().getParcelable("creator");
		
		
		if(imageLoader == null){
			imageLoader = AppController.getInstance().getImageLoader();
		}
		
		//getActionBar().hide();

		tabAdapter = new CreatorTabAdapter(getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.creatorPager);
		viewPager.setAdapter(tabAdapter);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setViewPager(viewPager);

		profileImageView = (NetworkImageView) findViewById(R.id.profie_image_view3);
		profileImageView.setDefaultImageResId(R.drawable.default_profic);
		profileImageView.setImageUrl(Constants.urlgetImgServlet+creator.getImageUrl(), imageLoader);
	}
}
