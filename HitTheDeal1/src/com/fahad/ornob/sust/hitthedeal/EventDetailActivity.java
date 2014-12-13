package com.fahad.ornob.sust.hitthedeal;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fahad.ornob.sust.hitthedeal.adapter.EventFeedBackAdapter;
import com.fahad.ornob.sust.hitthedeal.adapter.FeedListAdapter;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.customImageView.ScrollViewX;
import com.fahad.ornob.sust.hitthedeal.customImageView.ScrollViewX.OnScrollViewListener;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.FeedBackItem;
import com.fahad.ornob.sust.hitthedeal.item.RatingResultItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class EventDetailActivity extends Activity {
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	RatingBar getRatingBar;
	RatingBar setRatingBar;

	float curRate;
	int count;
	float newrating;
	int myViwerRating;

	NetworkImageView eventFeedImgView;
	NetworkImageView organizationEventProPic;
	TextView eventNameEvDTxt, orgNameEvDTxt, eventStartTimeEvDTxt,
			eventEndTimeEvDTxt, ratingStampEvDTxt, ratingPeopleNumberEvDTxt,
			eventDescEvDTxt;

	ListView feedEvDListView;
	EditText giveFeedBackEvDEd;
	Button giveFeedBackEvDB;
	TextView showMapTxt;

	private static final String TAG = EventDetailActivity.class.getSimpleName();
	private EventFeedBackAdapter listAdapter;

	private ArrayList<FeedBackItem> feedbackItems;
	private ArrayList<UserItem> userItems;

	Event eventItem;
	UserItem userItem;
	RatingResultItem ratingResultItem;
	ConnectionDetector cd;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detail);

		int selectedEventId = getIntent().getIntExtra("selectedEventId", 1);
		
		
		final ColorDrawable cd = new ColorDrawable(Color.rgb(60, 184, 121 ));
		mActionBar = getActionBar();
		mActionBar.setBackgroundDrawable(cd);		
		cd.setAlpha(0);
		mActionBar.setDisplayHomeAsUpEnabled(true); //to activate back pressed on home button press
		mActionBar.setDisplayShowHomeEnabled(false); //
		mActionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'>Event Detail</font></b>"));
		ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.event_detail_fahad_scroll);
		scrollView.setOnScrollViewListener(new OnScrollViewListener() {
			
			@Override
			public void onScrollChanged(ScrollViewX v, int l, int t, int oldl, int oldt) {
				
				cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
			}

			private int getAlphaforActionBar(int scrollY) {
				int minDist = 0,maxDist = 650;
				if(scrollY>maxDist){ 
					return 255;
					}
				else if(scrollY<minDist){
					return 0;
					}
				else {
					int alpha = 0;
					alpha = (int)  ((255.0/maxDist)*scrollY);
					return alpha;
				}
			}
		});
		
		

		Init();
		loadData(selectedEventId);
		startTimer();
		listener();
	}

	private void loadData(int selectedEventId) {
		// http://192.168.2.105:8084/AndroidLogin/GetSelectedEventDetail?event_id=1

		GetDataTask(
				Constants.urlGetSelectedEventDetail + "event_id="
						+ selectedEventId + "&viewer_id="
						+ Constants.userItem.getUser_id() + "",
				DataBaseKeys.GetEventDetailType);
	}

	private void addItemToFeedListView() {
		// TODO Auto-generated method stub
		listAdapter = new EventFeedBackAdapter(this, feedbackItems, userItems);
		feedEvDListView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
	}

	private void Init() {
		// TODO Auto-generated method stub
		getRatingBar = (RatingBar) findViewById(R.id.getRating);
		setRatingBar = (RatingBar) findViewById(R.id.setRating);

		eventFeedImgView = (NetworkImageView) findViewById(R.id.eventFeedImgView);
		eventFeedImgView.setDefaultImageResId(R.drawable.event_default);
		organizationEventProPic = (NetworkImageView) findViewById(R.id.organizationEventProPic);
		organizationEventProPic.setDefaultImageResId(R.drawable.default_profic);
		

		eventNameEvDTxt = (TextView) findViewById(R.id.eventNameEvDTxt);
		orgNameEvDTxt = (TextView) findViewById(R.id.orgNameEvDTxt);
		eventStartTimeEvDTxt = (TextView) findViewById(R.id.eventStartTimeEvDTxt);
		eventEndTimeEvDTxt = (TextView) findViewById(R.id.eventEndTimeEvDTxt);
		ratingStampEvDTxt = (TextView) findViewById(R.id.ratingStampEvDTxt);
		ratingPeopleNumberEvDTxt = (TextView) findViewById(R.id.ratingPeopleNumberEvDTxt);
		eventDescEvDTxt = (TextView) findViewById(R.id.eventDescEvDTxt);

		giveFeedBackEvDEd = (EditText) findViewById(R.id.giveFeedBackEvDEd);
		giveFeedBackEvDB = (Button) findViewById(R.id.giveFeedBackEvDB);

		cd = new ConnectionDetector(this);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		feedbackItems = new ArrayList<FeedBackItem>();
		userItems = new ArrayList<UserItem>();
		feedEvDListView = (ListView) findViewById(R.id.feedEvDListView);

		listAdapter = new EventFeedBackAdapter(this, feedbackItems, userItems);
		feedEvDListView.setAdapter(listAdapter);

		showMapTxt = (TextView) findViewById(R.id.showMapTxt);
		initMap();
	}

	private void addItemToComponent() {
		// TODO Auto-generated method stub
		setRatingBar.setRating((float) ratingResultItem.getRating());
		curRate = (float) ratingResultItem.getRating();
		count = ratingResultItem.getCountNumber();

		eventFeedImgView.setImageUrl(Constants.urlgetImgServlet+eventItem.getEvent_img(), imageLoader);
		
		organizationEventProPic.setImageUrl(Constants.urlgetImgServlet+userItem.getImage_url(),
				imageLoader);

		eventNameEvDTxt.setText(eventItem.getEvent_name());
		orgNameEvDTxt.setText(userItem.getUser_name());

		CharSequence timeSt = DateUtils.getRelativeTimeSpanString(
				eventItem.getStartDate(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		CharSequence timeFn = DateUtils.getRelativeTimeSpanString(
				eventItem.getEndDate(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		eventStartTimeEvDTxt.setText(timeSt);
		eventEndTimeEvDTxt.setText(timeFn);

		ratingStampEvDTxt.setText(String.valueOf(roundMyData(
				ratingResultItem.getRating(), 1)));
		ratingPeopleNumberEvDTxt.setText(String.valueOf(ratingResultItem
				.getCountNumber()));
		eventDescEvDTxt.setText(eventItem.getEventDescription());

		getRatingBar.setRating((float) myViwerRating);
	}

	String feed="";
	private void listener() {
		// TODO Auto-generated method stub
		getRatingBar
				.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						newrating = rating;
						String url = Constants.urlInserRating + "event_id="
								+ eventItem.getEventId() + "&viewer_id="
								+ Constants.userItem.getUser_id() + "&rating="
								+ rating + "";
						GetDataTask(url, DataBaseKeys.InsertRatingType);

					}
				});

		giveFeedBackEvDB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				feed = giveFeedBackEvDEd.getText().toString();
				if (!feed.isEmpty()) {
					Toast.makeText(EventDetailActivity.this, "start:" + feed,
							Toast.LENGTH_SHORT).show();
					String url = Constants.urlInsertFeedBack; /*+ "event_id="
							+ eventItem.getEventId() + "&viewer_id="
							+ Constants.userItem.getUser_id() + "&feedback="
							+ feed + "&date="
							+ CommonMethod.currentTimeFrom1970() + "&setFlag=1";*/
					GetDataTaskForFeedBack(url, DataBaseKeys.InserFeedBackType);
				}
			}
		});

		feedEvDListView.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle ListView touch events.
				v.onTouchEvent(event);
				return true;
			}
		});

		showMapTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (showMapTxt.getText().toString().equals("Show Map")) {
					expandOrCollapse(
							findViewById(R.id.map_fragment_eventDetailId),
							"expand");
					showMapTxt.setText("Close Map");
				} else {
					expandOrCollapse(
							findViewById(R.id.map_fragment_eventDetailId),
							"close");
					showMapTxt.setText("Show Map");
				}
			}
		});
		
		organizationEventProPic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("creatorId", userItem.getUser_id());
				bundle.putInt("vId", Constants.userItem.getUser_id());
				bundle.putString("vName", userItem.getUser_name());
				bundle.putString("vImgUrl", userItem.getImage_url());

				Intent intent = new Intent(EventDetailActivity.this,
						FavouriteCreatorActivityOrnob.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	private void GetDataTask(String url, final int typeItem) {
		// TODO Auto-generated method stub
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(url);

		if (cd.isConnectingToInternet()) {
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET, url,
					null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {

								parseJsonFeed(response, typeItem);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
							Toast.makeText(EventDetailActivity.this,
									error.toString(), Toast.LENGTH_SHORT)
									.show();
						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		} else if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data), -1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void GetDataTaskForFeedBack(String url, final int typeItem) {
		// TODO Auto-generated method stub
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(url);

		if (cd.isConnectingToInternet()) {
			StringRequest jsonReq = new StringRequest(Method.POST, url
					, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {

								JSONObject json;
								try {
									json = new JSONObject(response);
									parseJsonFeed(json, typeItem);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
							Toast.makeText(EventDetailActivity.this,
									error.toString(), Toast.LENGTH_SHORT)
									.show();
						}
					}){

				@Override
				protected Map<String, String> getParams() {
					if(typeItem== DataBaseKeys.InserFeedBackType){
						Map<String, String> params = new HashMap<String, String>();
						params.put("event_id",String.valueOf(eventItem.getEventId()));
						params.put("viewer_id", String.valueOf(Constants.userItem.getUser_id()));
						params.put("feedback", feed);
						params.put("date", String.valueOf(CommonMethod.currentTimeFrom1970()));
						params.put("setFlag", "1");
	
						return params;
					}
					return null;
				}
			};

			
			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		}
	}

	private void parseJsonFeed(JSONObject response, int itemType) {
		try {
			int success = response.getInt(DataBaseKeys.Success);
			if (success == 1) {

				if (itemType == DataBaseKeys.GetEventDetailType) {
					JSONObject eventJsonObject = response
							.getJSONObject(DataBaseKeys.EventDetaiTag);

					eventItem = new Event(
							eventJsonObject.getInt(DataBaseKeys.EVENT_ID),
							eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
							eventJsonObject.getString(DataBaseKeys.EVENT_NAME),
							eventJsonObject
									.getString(DataBaseKeys.EVENT_DESCRIPTIOPN),
							eventJsonObject.getLong(DataBaseKeys.START_DATE),
							eventJsonObject.getLong(DataBaseKeys.END_DATE),
							eventJsonObject.getDouble(DataBaseKeys.LATITUDE),
							eventJsonObject.getDouble(DataBaseKeys.LONGITUDE),
							eventJsonObject.getString(DataBaseKeys.EVENT_IMG),
							eventJsonObject.getString(DataBaseKeys.EVENT_URL));

					userItem = new UserItem(
							eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
							eventJsonObject.getString(DataBaseKeys.USER_NAME),
							eventJsonObject
									.getString(DataBaseKeys.USER_IMAGE_URL),
							eventJsonObject
									.getInt(DataBaseKeys.USER_CREATOR_TYPE_ID));

					JSONObject ratingJsonObject = response
							.getJSONObject(DataBaseKeys.RatingDetailTag);
					ratingResultItem = new RatingResultItem(
							ratingJsonObject.getDouble(DataBaseKeys.RatingTag),
							ratingJsonObject
									.getInt(DataBaseKeys.RatingCounNumberTag));

					JSONObject feedbackJsonObject = response
							.getJSONObject(DataBaseKeys.FeedBackJsonObjTag);
					int feedbacksuccess = feedbackJsonObject
							.getInt(DataBaseKeys.FeedBackSuccessTAG);
					if (feedbacksuccess == 1) {
						JSONArray feedbackJsonArray = feedbackJsonObject
								.getJSONArray(DataBaseKeys.FeedBackJsonArrayTag);
						for (int i = 0; i < feedbackJsonArray.length(); i++) {
							JSONObject feedbackObjectItem = (JSONObject) feedbackJsonArray
									.get(i);

							FeedBackItem item = new FeedBackItem(
									feedbackObjectItem
											.getInt(DataBaseKeys.FeedBackTag[0]),
									feedbackObjectItem
											.getInt(DataBaseKeys.FeedBackTag[1]),
									feedbackObjectItem
											.getInt(DataBaseKeys.FeedBackTag[2]),
									feedbackObjectItem
											.getString(DataBaseKeys.FeedBackTag[3]),
									feedbackObjectItem
											.getLong(DataBaseKeys.FeedBackTag[4]));
							feedbackItems.add(item);

							UserItem userItem = new UserItem(
									feedbackObjectItem
											.getInt(DataBaseKeys.FeedBackTag[2]),
									feedbackObjectItem
											.getString(DataBaseKeys.USER_NAME),
									feedbackObjectItem
											.getString(DataBaseKeys.USER_IMAGE_URL),
									1);
							userItems.add(userItem);
						}
					}

					myViwerRating = response
							.getInt(DataBaseKeys.MYViwerRatingTag);

					addItemToComponent();
					listAdapter.notifyDataSetChanged();

					double latitude = eventItem.getLatitude();
					double longitude = eventItem.getLongitude();
					latLng = new LatLng(latitude, longitude);
					MarkerOptions marker = new MarkerOptions().position(latLng)
							.title(eventItem.getEvent_name());
					myMap.addMarker(marker);
					myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
							15.0f));

				} else if (itemType == DataBaseKeys.InsertRatingType) {

					Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
					DecimalFormat decimalFormat = new DecimalFormat("#.#");
					JSONObject ratingJsonObject = response
							.getJSONObject(DataBaseKeys.RatingDetailTag);
					RatingResultItem ratingResultItem = new RatingResultItem(
							ratingJsonObject.getDouble(DataBaseKeys.RatingTag),
							ratingJsonObject
									.getInt(DataBaseKeys.RatingCounNumberTag));
					curRate = (float) ratingResultItem.getRating();
					setRatingBar.setRating(curRate);
					ratingStampEvDTxt.setText(String.valueOf(roundMyData(
							ratingResultItem.getRating(), 1)));
					ratingPeopleNumberEvDTxt.setText(String
							.valueOf(ratingResultItem.getCountNumber()));

				}else if (itemType == DataBaseKeys.GetFeedBackType) {

					Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

					/*JSONObject feedbackJsonObject = response
							.getJSONObject(DataBaseKeys.FeedBackJsonObjTag);*/
					JSONArray feedbackJsonArray = response
							.getJSONArray("all");
					feedbackItems.clear();
					for (int i = 0; i < feedbackJsonArray.length(); i++) {
						JSONObject feedbackObjectItem = (JSONObject) feedbackJsonArray
								.get(i);

						FeedBackItem item = new FeedBackItem(
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[0]),
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[1]),
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[2]),
								feedbackObjectItem
										.getString(DataBaseKeys.FeedBackTag[3]),
								feedbackObjectItem
										.getLong(DataBaseKeys.FeedBackTag[4]));
						feedbackItems.add(item);

						UserItem userItem = new UserItem(
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[2]),
								feedbackObjectItem
										.getString(DataBaseKeys.USER_NAME),
								feedbackObjectItem
										.getString(DataBaseKeys.USER_IMAGE_URL),
								1);
						userItems.add(userItem);
					}

					giveFeedBackEvDEd.setText("");

					addItemToFeedListView();
					scrollMyListViewToBottom();
					
				}else if (itemType == DataBaseKeys.InserFeedBackType) {

					Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

					JSONObject feedbackJsonObject = response
							.getJSONObject(DataBaseKeys.FeedBackJsonObjTag);
					JSONArray feedbackJsonArray = feedbackJsonObject
							.getJSONArray(DataBaseKeys.FeedBackJsonArrayTag);
					feedbackItems.clear();
					for (int i = 0; i < feedbackJsonArray.length(); i++) {
						JSONObject feedbackObjectItem = (JSONObject) feedbackJsonArray
								.get(i);

						FeedBackItem item = new FeedBackItem(
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[0]),
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[1]),
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[2]),
								feedbackObjectItem
										.getString(DataBaseKeys.FeedBackTag[3]),
								feedbackObjectItem
										.getLong(DataBaseKeys.FeedBackTag[4]));
						feedbackItems.add(item);

						UserItem userItem = new UserItem(
								feedbackObjectItem
										.getInt(DataBaseKeys.FeedBackTag[2]),
								feedbackObjectItem
										.getString(DataBaseKeys.USER_NAME),
								feedbackObjectItem
										.getString(DataBaseKeys.USER_IMAGE_URL),
								1);
						userItems.add(userItem);
					}

					giveFeedBackEvDEd.setText("");

					addItemToFeedListView();
					scrollMyListViewToBottom();
				}

			} else {
				Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {

			e.printStackTrace();
			Toast.makeText(EventDetailActivity.this, e.toString(),
					Toast.LENGTH_SHORT).show();
			Log.e("hseon exception", e.toString());

		}

	}

	public void expandOrCollapse(final View v, String exp_or_colpse) {
		TranslateAnimation anim = null;
		if (exp_or_colpse.equals("expand")) {
			anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
			v.setVisibility(View.VISIBLE);
		} else {
			anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
			AnimationListener collapselistener = new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					v.setVisibility(View.GONE);
				}
			};

			anim.setAnimationListener(collapselistener);
		}

		// To Collapse
		//

		anim.setDuration(300);
		anim.setInterpolator(new AccelerateInterpolator(0.5f));
		v.startAnimation(anim);
	}

	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	Location myLocation;
	LatLng latLng = null;
	Marker marker = null;

	private void initMap() {
		// TODO Auto-generated method stub
		Button closeMapB = (Button) findViewById(R.id.closeB);
		closeMapB.setVisibility(View.GONE);
		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment) myFragmentManager
				.findFragmentById(R.id.mapCreatorSignUp);
		myMap = myMapFragment.getMap();
		myMap.setMyLocationEnabled(true);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {

		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					RQS_GooglePlayServices);
		}

	}

	public static double roundMyData(double Rval, int numberOfDigitsAfterDecimal) {
		double p = (float) Math.pow(10, numberOfDigitsAfterDecimal);
		Rval = Rval * p;
		double tmp = Math.floor(Rval);

		return (double) tmp / p;
	}

	private void scrollMyListViewToBottom() {
		feedEvDListView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				if (listAdapter.getCount() != 0) {
					feedEvDListView.smoothScrollToPosition(listAdapter
							.getCount() - 1);
				}
				// Just add something to scroll to the top ;-)
			}
		});
	}

	Timer timer = null;
	TimerTask timerTask;

	void startTimer() {
		timer = new Timer();
		initializeTimerTask();
		timer.scheduleAtFixedRate(timerTask, 30000, 30000);
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
				EventDetailActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						String url = Constants.urlGetEventFeedBack + "event_id="
								+ eventItem.getEventId();
						GetDataTask(url, DataBaseKeys.GetFeedBackType);
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

}
