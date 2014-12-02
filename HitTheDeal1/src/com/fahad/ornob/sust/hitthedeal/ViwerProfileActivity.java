package com.fahad.ornob.sust.hitthedeal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fahad.ornob.sust.hitthedeal.adapter.MyCreatorListViewAdapter;
import com.fahad.ornob.sust.hitthedeal.adapter.TabsPagerAdapter;
import com.fahad.ornob.sust.hitthedeal.adapter.ViewerProfileTabPagerAdapter;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.fragment.GoogleMapFragment;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.Marker;

public class ViwerProfileActivity extends Activity {

	//FeedImageView myProImageView;
	TextView viwerNameProfileTxt;
	NetworkImageView myProfileNetImgView;
	Button myProfileB,myFavCreatB;
	// Tab titles//floating_button_viwer_class
	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	FloatingActionButton floatingActionButton;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viwer_profile_fragment);
		initMyProfile();
		initMyFavCreator();
		
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		
		viwerNameProfileTxt = (TextView)findViewById(R.id.viwerNameProfileTxt);
		myProfileNetImgView = (NetworkImageView)findViewById(R.id.myProImageView);
		myProfileNetImgView.setDefaultImageResId(R.drawable.default_profic);
		
		myProfileB= (Button)findViewById(R.id.profileInfoB);
		myFavCreatB= (Button)findViewById(R.id.myfavCreatorsB);
		
		viwerNameProfileTxt.setText(Constants.userItem.getUser_name());
		
		myProfileNetImgView.setImageUrl(Constants.urlgetImgServlet+Constants.userItem.getImage_url(), imageLoader);
		
		myProfileB.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findViewById(R.id.my_profile_info_inc_id).setVisibility(View.VISIBLE);
				findViewById(R.id.my_fav_creat_inc_id).setVisibility(View.INVISIBLE);
				myProfileB.setBackgroundColor(getResources().getColor(R.color.offwhite));
				myFavCreatB.setBackgroundColor(getResources().getColor(R.color.offlightwhite));
				
				YoYo.with(Techniques.BounceIn) .duration(700)
				 .playOn(findViewById(R.id.my_profile_info_inc_id));
				YoYo.with(Techniques.FadeIn) .duration(800)
				 .playOn(findViewById(R.id.floating_button_viwer_class)); 
				 
			}
		});
		myFavCreatB.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myProfileB.setBackgroundColor(getResources().getColor(R.color.offlightwhite));
				myFavCreatB.setBackgroundColor(getResources().getColor(R.color.offwhite));
				
				findViewById(R.id.my_profile_info_inc_id).setVisibility(View.INVISIBLE);
				findViewById(R.id.my_fav_creat_inc_id).setVisibility(View.VISIBLE);
				 
				YoYo.with(Techniques.BounceIn) .duration(700)
				 .playOn(findViewById(R.id.my_fav_creat_inc_id));
				 YoYo.with(Techniques.FadeOut) .duration(800)
				 .playOn(findViewById(R.id.floating_button_viwer_class)); 
			}
		});
		
		floatingActionButton = (FloatingActionButton)findViewById(R.id.floating_button_viwer_class);
		floatingActionButton.setColor(Color.parseColor("#FF9500"));
		floatingActionButton.setDrawable(getResources().getDrawable(
				R.drawable.floating_button_icon));
		floatingActionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViwerProfileActivity.this,
						EditViewerProfileActivity.class);
				startActivity(intent);
			}
		});
	}

	

	TextView emailMyProfileFrgTxt,addressMyProfileFrgTxt,phoneMyProfileFrgTxt,dateCreatMyProfileFrgTxt;
	private void initMyProfile() {
		// TODO Auto-generated method stub
		emailMyProfileFrgTxt =(TextView)  findViewById(R.id.emailMyProfileFrgTxt); 
		addressMyProfileFrgTxt =(TextView)  findViewById(R.id.addressMyProfileFrgTxt); 
		phoneMyProfileFrgTxt =(TextView)  findViewById(R.id.phoneMyProfileFrgTxt); 
		dateCreatMyProfileFrgTxt=(TextView)  findViewById(R.id.dateCreatMyProfileFrgTxt);
		
		emailMyProfileFrgTxt.setText(Constants.userItem.getEmail() ); 
		if(!Constants.userItem.getAddress().isEmpty()){
			addressMyProfileFrgTxt.setText(Constants.userItem.getAddress() ); 
		}else addressMyProfileFrgTxt.setText("Please add your address.");
		if(!Constants.userItem.getPhn_no().isEmpty())
			phoneMyProfileFrgTxt.setText(Constants.userItem.getPhn_no() );
		else phoneMyProfileFrgTxt.setText("Please add your phone number." );
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Constants.userItem.getDate_of_creation() ,
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		dateCreatMyProfileFrgTxt.setText(timeAgo);
	}
		
	private static final String TAG = ViwerProfileActivity.class.getSimpleName();
	private ListView listView;
	private MyCreatorListViewAdapter listAdapter;
	private ArrayList<UserItem> userItems;
	ConnectionDetector cd;
	
	private void initMyFavCreator() {
		// TODO Auto-generated method stub
		cd = new ConnectionDetector(ViwerProfileActivity.this);
		
		userItems = new ArrayList<UserItem>();
		
		listView = (ListView) findViewById(R.id.myCreatorListProfileFrgListView);
	
	
		listAdapter = new MyCreatorListViewAdapter(ViwerProfileActivity.this, userItems);
		listView.setAdapter(listAdapter);
		
		
		GetDataTask(Constants.urlGetMyFavCreator+"viewer_id="+Constants.userItem.getUser_id());

						
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Event eventItem = eventItems.get(position-1);
				Toast.makeText(ViwerProfileActivity.this, "position:"+position+" , id: ", Toast.LENGTH_SHORT).show();
				/*
			    Intent intent = new Intent(ViwerProfileActivity.this,EventDetailActivity.class);
			    intent.putExtra("selectedEventId", eventItem.getEventId());
			    startActivity(intent);*/
			}
		});
	}
	
	
	private void GetDataTask(String url) {
		// TODO Auto-generated method stub
	
		
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(url);
		
		if(cd.isConnectingToInternet()){
			Toast.makeText(ViwerProfileActivity.this,"sdsd", Toast.LENGTH_SHORT).show();
			
			
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					url, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								
								parseJsonFeed(response,-1);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());

						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		}else if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data),-1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
	}



	private  void parseJsonFeed(JSONObject response,int itemType) {
		try {
			
			 int success = response.getInt(DataBaseKeys.Success);
			 if(success==1){

				 JSONArray eventsJsonArray = response.getJSONArray("all");

					for (int i = 0; i < eventsJsonArray.length(); i++) {
						JSONObject eventJsonObject = (JSONObject) eventsJsonArray.get(i);						
						//public static final String  userKey[] = {"user_id", "user_type_id", "user_name", "address", "email", "phn_no", "date_of_creation", "latitude", "longitude", "image_url", "password", "creator_type_id"};
						
						UserItem userItem = new UserItem(eventJsonObject.getInt(DataBaseKeys.userKey[0]), eventJsonObject.getString(DataBaseKeys.userKey[2]), eventJsonObject.getString(DataBaseKeys.userKey[9]), eventJsonObject.getInt(DataBaseKeys.userKey[1]));
						userItems.add(userItem);
												
						
						
					}
					
					listAdapter = new MyCreatorListViewAdapter(ViwerProfileActivity.this, userItems);
					listView.setAdapter(listAdapter);
					listAdapter.notifyDataSetChanged();					
				 
			 }else{
				 Toast.makeText(ViwerProfileActivity.this, "Fail", Toast.LENGTH_SHORT).show();
			 }
			
		} catch (JSONException e) {

			
			e.printStackTrace();
		}
		
	}
		
}