package com.fahad.ornob.sust.hitthedeal;

import com.fahad.ornob.sust.hitthedeal.app.AppController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import necessaryMethods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.FeedListAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fahad.ornob.sust.hitthedeal.R;

import constants.DBKeys;
import databaseEntities.Creator1;
import databaseEntities.Event;
import databaseEntities.RatingResultItem;

@SuppressLint("NewApi")
public class CreatorEventsActivityOrnob extends Activity {

	Creator1 creator;
	ListView listView;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creator_event);

		Bundle bundle = getIntent().getExtras();
		creator = bundle.getParcelable("creator");

		listView = (ListView) findViewById(R.id.list);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		makeVolleyRequest(listView, progressBar);
	}

	void makeVolleyRequest(final ListView listView,
			final ProgressBar progressBar) {
		Cache cache = AppController.getInstance().getRequestQueue().getCache();

		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "GetCreatorEvent",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray eventsArray = new JSONArray(response);
								ArrayList<Event> events = jsonToArrayLiast(eventsArray);
								ArrayList<RatingResultItem> ratings = jsonRatingToArrayLiast(eventsArray);
								FeedListAdapter listAdapter = new FeedListAdapter(
										CreatorEventsActivityOrnob.this, events,
										creator, ratings);
								progressBar.setVisibility(View.INVISIBLE);
								listView.setAdapter(listAdapter);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Toast.makeText(CreatorEventsActivityOrnob.this,
									"Database Fetching Error",
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// Methods.makeToast(CreatorEventsActivity.this,
						// "Network Error", Toast.LENGTH_LONG);
						progressBar.setVisibility(View.VISIBLE);
						Methods.makeToast(CreatorEventsActivityOrnob.this,
								error.toString(), Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("creator_id", Integer.toString(creator.getUserId()));

				return params;
			}

		};

		AppController.getInstance().addToRequestQueue(strReq);
	}

	ArrayList<Event> jsonToArrayLiast(JSONArray eventsJsonArray) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (int i = 0; i < eventsJsonArray.length(); i = i + 2) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				Event event = new Event(
						eventJsonObject.getInt(DBKeys.EVENT_ID),
						eventJsonObject.getInt(DBKeys.CREATOR_ID),
						eventJsonObject
								.getString(DBKeys.EVENT_DESCRIPTIOPN),
						eventJsonObject.getLong(DBKeys.START_DATE),
						eventJsonObject.getLong(DBKeys.END_DATE),
						eventJsonObject.getDouble(DBKeys.LATITUDE),
						eventJsonObject.getDouble(DBKeys.LONGITUDE),
						eventJsonObject.getString(DBKeys.EVENT_NAME),
						eventJsonObject.getString(DBKeys.EVENT_IMG),
						eventJsonObject.getString(DBKeys.EVENT_URL));
				events.add(event);
			} catch (JSONException e) {
				Methods.makeToast(CreatorEventsActivityOrnob.this, e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		// Methods.makeToast(CreatorEventsActivity.this, events.size() + "",
		// Toast.LENGTH_LONG);
		return events;
	}

	ArrayList<RatingResultItem> jsonRatingToArrayLiast(JSONArray eventsJsonArray) {
		ArrayList<RatingResultItem> ratings = new ArrayList<RatingResultItem>();
		for (int i = 1; i < eventsJsonArray.length(); i = i + 2) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				RatingResultItem event = new RatingResultItem(
						eventJsonObject.getInt("rating"),
						eventJsonObject.getInt("rating_count"));
				ratings.add(event);
			} catch (JSONException e) {
				Methods.makeToast(CreatorEventsActivityOrnob.this, e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		// Methods.makeToast(CreatorEventsActivity.this, ratings.size() + "",
		// Toast.LENGTH_LONG);
		return ratings;
	}
}
