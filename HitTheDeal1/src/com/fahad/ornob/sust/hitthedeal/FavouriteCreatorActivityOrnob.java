package com.fahad.ornob.sust.hitthedeal;

import java.util.HashMap;
import java.util.Map;

import necessaryMethods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.FavouriteCreatorTabAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import constants.DBKeys;
import customviews.PagerSlidingTabStrip;
import databaseEntities.Creator1;
import fragments.FragmentFavourtieCreatorAbout;
import fragments.FragmentFavouriteCreatorEvents;

@SuppressLint({ "NewApi", "ResourceAsColor", "ClickableViewAccessibility" })
public class FavouriteCreatorActivityOrnob extends FragmentActivity {

	NetworkImageView profileImageView;
	FrameLayout anchorLayout;
	public static Creator1 creator;
	public static int vId;
	public static String vName;
	public static String vImgUrl;
	TextView nameTv, addressTv, phnTv, emailTv, typeTv;
	ViewPager viewPager;
	FavouriteCreatorTabAdapter tabAdapter;
	PagerSlidingTabStrip tabs;
	int creatorId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creator);

		// creator = new Creator1(4, "Abu Shahriar Ratul", "sylhet",
		// "ratul@gmail.com", "1234567", 1234, 24.8997635, 91.8619037,
		// "http://api.androidhive.info/feed/img/life.jpg", 2);

		// creatorId = 4;
		// vId = 1;
		// vName = "Fahad";
		// vImgUrl = "http://api.androidhive.info/feed/img/life.jpg";

		creatorId = getIntent().getExtras().getInt("creatorId");
		vId = getIntent().getExtras().getInt("vId");
		vName = getIntent().getExtras().getString("vName");
		vImgUrl = getIntent().getExtras().getString("vImgUrl");

		getActionBar().hide();
		profileImageView = (NetworkImageView) findViewById(R.id.profie_image_view);
		//profileImageView.setDefaultImageResId(R.drawable.default_profile_image);
		fetchCreatorData();
		createFragments();
	}

	void createFragments() {
		tabAdapter = new FavouriteCreatorTabAdapter(getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.creatorPager);
		viewPager.setAdapter(tabAdapter);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setViewPager(viewPager);
	}

	void fetchCreatorData() {
		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "GetCreatorDetails",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONObject jsonObject = new JSONObject(response);

								if (jsonObject.getBoolean("success") == true) {
									// progressBar.setVisibility(View.INVISIBLE);
									JSONArray dataArray = jsonObject
											.getJSONArray("data");
									JSONObject dataObject = (JSONObject) dataArray
											.get(0);

									creator = new Creator1(
											dataObject.getInt(DBKeys.USER_ID),
											dataObject
													.getString(DBKeys.USER_NAME),
											dataObject
													.getString(DBKeys.ADDRESS),
											dataObject.getString(DBKeys.EMAIL),
											dataObject.getString(DBKeys.PHN_NO),
											dataObject
													.getLong(DBKeys.DATE_0F_CREATION),
											dataObject
													.getDouble(DBKeys.LATITUDE),
											dataObject
													.getDouble(DBKeys.LONGITUDE),
											dataObject
													.getString(DBKeys.IMAGE_URL),
											dataObject
													.getInt(DBKeys.CREATOR_TYPE_ID));
									FragmentFavourtieCreatorAbout
											.updateProfieViews();
									FragmentFavouriteCreatorEvents
											.makeVolleyRequest();
									// Methods.makeToast(
									// FavouriteCreatorActivity.this,
									// creator.getUserName(),
									// Toast.LENGTH_LONG);
								} else {
									// progressBar.setVisibility(View.INVISIBLE);
								}

							} catch (JSONException e) {
								// progressBar.setVisibility(View.INVISIBLE);
								Methods.makeToast(
										FavouriteCreatorActivityOrnob.this,
										"Creator Data Fetch Error",
										Toast.LENGTH_LONG);
							}
						} else {

						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// NetworkResponse networkResponse =
						// error.networkResponse;
						// if (networkResponse == null) {
						// Methods.makeToast(FavouriteCreatorActivity.this,
						// "network null", Toast.LENGTH_LONG);
						// }
						// progressBar.setVisibility(View.INVISIBLE);
						Methods.makeToast(FavouriteCreatorActivityOrnob.this,
								error.toString(), Toast.LENGTH_LONG);
						// Methods.makeToast(CreateEventActivity.this,
						// "Network Error", Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("creator_id", Integer.toString(creatorId));

				return params;
			}
		};

		Volley.newRequestQueue(FavouriteCreatorActivityOrnob.this).add(strReq);
	}
}
