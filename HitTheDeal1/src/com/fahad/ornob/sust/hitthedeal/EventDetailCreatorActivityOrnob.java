package com.fahad.ornob.sust.hitthedeal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import necessaryMethods.Methods;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.customImageView.ScrollViewX;
import com.fahad.ornob.sust.hitthedeal.customImageView.ScrollViewX.OnScrollViewListener;
import com.fahad.ornob.sust.hitthedeal.fragment.GoogleMapFragment;

import adapter.EventFeedBackAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fahad.ornob.sust.hitthedeal.R;
import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import customviews.FeedImageView;
import databaseEntities.Creator1;
import databaseEntities.Event;
import databaseEntities.FeedBackItem;
import databaseEntities.FeedbackMaker;
import databaseEntities.RatingResultItem;

public class EventDetailCreatorActivityOrnob extends Activity implements
		View.OnClickListener {

	Timer timer = null;
	TimerTask timerTask;
	Animation slideIn, slideOut;
	float curRate;
	int count;
	float newrating;
	GoogleMap googleMap;
	FrameLayout mapLayout;
	NetworkImageView eventFeedImgView;
	NetworkImageView organizationEventProPic;
	TextView eventNameEvDTxt, orgNameEvDTxt, eventStartTimeEvDTxt,
			eventEndTimeEvDTxt, ratingStampEvDTxt, ratingPeopleNumberEvDTxt,
			eventDescEvDTxt;

	ListView feedEvDListView;
	EditText giveFeedBackEvDEd;
	Button giveFeedBackEvDB, mapButton, openMapButton, exitMap;
	FloatingActionButton floatingActionButton;
	Creator1 creator;
	ArrayList<FeedBackItem> feedBacks;
	ArrayList<FeedbackMaker> feedbackMakers;

	private static final String TAG = EventDetailCreatorActivityOrnob.class.getSimpleName();
	private EventFeedBackAdapter listAdapter;

	public static Event event;
	RatingResultItem rating;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	int vId;
	String vName, vImgUrl;
	boolean isCreator, fromNotification;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		final ColorDrawable cd = new ColorDrawable(Color.rgb(60, 184, 121 ));
		mActionBar = getActionBar();
		mActionBar.setBackgroundDrawable(cd);		
		cd.setAlpha(0);
		mActionBar.setDisplayHomeAsUpEnabled(true); //to activate back pressed on home button press
		mActionBar.setDisplayShowHomeEnabled(false); //
		mActionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'>Event Detail</font></b>"));
		ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.event_detail_ornob_scroll);
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
		
		
		
		
		event = getIntent().getExtras().getParcelable("event");
		rating = getIntent().getExtras().getParcelable("rating");
		creator = getIntent().getExtras().getParcelable("creator");
		isCreator = getIntent().getExtras().getBoolean("isCreator");
		fromNotification = getIntent().getExtras().getBoolean(
				"fromNotification");
		vId = getIntent().getExtras().getInt("vId");
		vName = getIntent().getExtras().getString("vName");
		vImgUrl = getIntent().getExtras().getString("vImgUrl");

		// Methods.makeToast(EventDetailCreatorActivity.this, vName,
		// Toast.LENGTH_LONG);

		slideIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_in);
		slideOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_out);

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map1)).getMap();
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(
				event.getLatitude(), event.getLongitude())), 15.0f));
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(event.getLatitude(), event.getLongitude())).title(
				event.getEventName()));

		mapLayout = (FrameLayout) findViewById(R.id.map_layout1);

		mapButton = (Button) findViewById(R.id.mapButton1);
		exitMap = (Button) findViewById(R.id.exit_map_button1);

		mapButton.setOnClickListener(this);
		exitMap.setOnClickListener(this);

		floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_button1);
		floatingActionButton.setColor(Color.parseColor("#FF9500"));
		floatingActionButton.setDrawable(getResources().getDrawable(
				R.drawable.floating_button_icon));
		floatingActionButton.setOnClickListener(this);

		if (!isCreator) {
			floatingActionButton.setVisibility(View.INVISIBLE);
		}
		Init();
		populateViews();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.mapButton1:
			slideMap();
			break;
		case R.id.exit_map_button1:
			slideMap();
			break;
		case R.id.floating_button1:
			Intent intent = new Intent(EventDetailCreatorActivityOrnob.this,
					EditEventActivityOrnob.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		populateViews();
		super.onResume();
	}

	private void Init() {
		// TODO Auto-generated method stub

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
		giveFeedBackEvDB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String feed = giveFeedBackEvDEd.getText().toString();
				if (!feed.isEmpty()) {
					insertFeedIntoDB(feed);
				}
			}
		});

		if (fromNotification) {
			giveFeedBackEvDEd.setVisibility(View.GONE);
			giveFeedBackEvDB.setVisibility(View.GONE);
		}
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		feedBacks = new ArrayList<FeedBackItem>();
		feedEvDListView = (ListView) findViewById(R.id.feedEvDListView);

		feedEvDListView.setAdapter(listAdapter);
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
		fetchFeedbackVolleyRequest();
		startTimer();
	}

	void slideMap() {
		if (mapLayout.getVisibility() == View.INVISIBLE) {
			mapLayout.setVisibility(View.VISIBLE);
			mapLayout.startAnimation(slideIn);

		} else {
			mapLayout.startAnimation(slideOut);
			mapLayout.setVisibility(View.INVISIBLE);
		}
	}

	private void populateViews() {
		// TODO Auto-generated method stub

		curRate = (float) rating.getRating();
		count = rating.getCountNumber();

		
		eventFeedImgView.setImageUrl(Constants.urlgetImgServlet+event.getEventImg(), imageLoader);
			
		organizationEventProPic.setImageUrl(Constants.urlgetImgServlet+creator.getImageUrl(), imageLoader);

		eventNameEvDTxt.setText(event.getEventName());
		orgNameEvDTxt.setText(creator.getUserName());

		CharSequence timeSt = DateUtils.getRelativeTimeSpanString(
				event.getStartDate(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		CharSequence timeFn = DateUtils.getRelativeTimeSpanString(
				event.getEndDate(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		eventStartTimeEvDTxt.setText(timeSt);
		eventEndTimeEvDTxt.setText(timeFn);

		ratingStampEvDTxt.setText(String.valueOf(rating.getRating()));
		ratingPeopleNumberEvDTxt
				.setText(String.valueOf(rating.getCountNumber()));
		eventDescEvDTxt.setText(event.getEventDescription());
	}

	void insertFeedIntoDB(final String feed) {
		final Calendar calender = Calendar.getInstance();
		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "InsertFeedback",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray jsonArray = new JSONArray(response);
								JSONObject jsonObject = (JSONObject) jsonArray
										.get(0);

								if (jsonObject.getBoolean("success")) {
									giveFeedBackEvDEd.setText("");
									fetchFeedbackVolleyRequest();					
								} else {
									Toast.makeText(EventDetailCreatorActivityOrnob.this,
											"Database Insertion Error",
											Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								Toast.makeText(EventDetailCreatorActivityOrnob.this,
										e.toString(), Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}
						} else {
							Toast.makeText(EventDetailCreatorActivityOrnob.this,
									"Database Fetching Error",
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Methods.makeToast(EventDetailCreatorActivityOrnob.this,
								"Network Error", Toast.LENGTH_LONG);
						Methods.makeToast(EventDetailCreatorActivityOrnob.this,
								error.toString(), Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("event_id", Integer.toString(event.getEventId()));
				params.put("viewer_id", Integer.toString(vId));
				params.put("feedback", feed);
				params.put("date", Long.toString(calender.getTimeInMillis()));

				return params;
			}

		};

		Volley.newRequestQueue(getApplicationContext()).add(strReq);
	}

	void fetchFeedbackVolleyRequest() {
		Cache cache = AppController.getInstance().getRequestQueue().getCache();

		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "GetFeedbacks",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray eventsArray = new JSONArray(response);
								jsonToArrayLiast(eventsArray);
								jsonFeedbackUserToArrayLiast(eventsArray);
								 listAdapter = new EventFeedBackAdapter(
										EventDetailCreatorActivityOrnob.this, feedBacks,
										feedbackMakers);
								feedEvDListView.setAdapter(listAdapter);
								scrollMyListViewToBottom();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Toast.makeText(EventDetailCreatorActivityOrnob.this,
									"Database Fetching Error",
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Methods.makeToast(EventDetailCreatorActivityOrnob.this,
								"Network Error", Toast.LENGTH_LONG);
						Methods.makeToast(EventDetailCreatorActivityOrnob.this,
								error.toString(), Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("event_id", Integer.toString(event.getEventId()));

				return params;
			}

		};

		Volley.newRequestQueue(getApplicationContext()).add(strReq);
	}

	void jsonToArrayLiast(JSONArray eventsJsonArray) {
		feedBacks = new ArrayList<FeedBackItem>();
		for (int i = 0; i < eventsJsonArray.length(); i = i + 2) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				FeedBackItem feedback = new FeedBackItem(
						eventJsonObject.getInt("feedback_id"),
						eventJsonObject.getInt("event_id"),
						eventJsonObject.getInt("viewer_id"),
						eventJsonObject.getString("feedback"),
						eventJsonObject.getLong("date"));
				feedBacks.add(feedback);
			} catch (JSONException e) {
				Methods.makeToast(EventDetailCreatorActivityOrnob.this, e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		// Methods.makeToast(CreatorEventsActivity.this, events.size() + "",
		// Toast.LENGTH_LONG);
	}

	void jsonFeedbackUserToArrayLiast(JSONArray eventsJsonArray) {
		feedbackMakers = new ArrayList<FeedbackMaker>();
		for (int i = 1; i < eventsJsonArray.length(); i = i + 2) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				FeedbackMaker feedbackMaker = new FeedbackMaker(
						eventJsonObject.getInt("user_id"),
						eventJsonObject.getString("user_name"),
						eventJsonObject.getString("image_url"));
				feedbackMakers.add(feedbackMaker);
			} catch (JSONException e) {
				Methods.makeToast(EventDetailCreatorActivityOrnob.this, e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		// Methods.makeToast(CreatorEventsActivity.this, ratings.size() + "",
		// Toast.LENGTH_LONG);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mapLayout.getVisibility() == View.VISIBLE) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void scrollMyListViewToBottom() {
		feedEvDListView.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	        	if (listAdapter.getCount() > 0) {
	        		feedEvDListView.smoothScrollToPosition(listAdapter.getCount() - 1);
				}
	            // Just add something to scroll to the top ;-)
	        }
	    });
	}
	
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
				EventDetailCreatorActivityOrnob.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						fetchFeedbackVolleyRequest();
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
