package fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import necessaryMethods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;

import adapter.FeedListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.fahad.ornob.sust.hitthedeal.CreatorActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.EventDetailCreatorActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.R;

import constants.DBKeys;
import databaseEntities.Event;
import databaseEntities.RatingResultItem;

public class FragmentCreatorEvents extends Fragment {

	ListView listView;
	ProgressBar progressBar;
	ArrayList<Event> eventList;
	ArrayList<RatingResultItem> ratingList;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events_creator, null);

		listView = (ListView) view.findViewById(R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Event event = eventList.get(position);
				RatingResultItem rating = ratingList.get(position);
				Intent intent = new Intent(getActivity(),
						EventDetailCreatorActivityOrnob.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("event", event);
				bundle.putParcelable("rating", rating);
				bundle.putParcelable("creator", CreatorActivityOrnob.creator);
				bundle.putBoolean("isCreator", true);
				bundle.putBoolean("fromNotification", false);
				bundle.putInt("vId", CreatorActivityOrnob.creator.getUserId());
				bundle.putString("vName", CreatorActivityOrnob.creator.getUserName());
				bundle.putString("vImgUrl",
						Constants.urlgetImgServlet+CreatorActivityOrnob.creator.getImageUrl());
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}

		});
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		makeVolleyRequest(listView, progressBar);
		return view;
	}

	void makeVolleyRequest(final ListView listView,
			final ProgressBar progressBar) {


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
										getActivity(), events,
										CreatorActivityOrnob.creator, ratings);
								progressBar.setVisibility(View.INVISIBLE);
								listView.setAdapter(listAdapter);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getActivity(),
									"Database Fetching Error",
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Methods.makeToast(getActivity(), "Network Error",
								Toast.LENGTH_LONG);
						progressBar.setVisibility(View.INVISIBLE);
						Methods.makeToast(getActivity(), error.toString(),
								Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("creator_id",
						Integer.toString(CreatorActivityOrnob.creator.getUserId()));

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
				Methods.makeToast(getActivity(), e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		// Methods.makeToast(CreatorEventsActivity.this, events.size() + "",
		// Toast.LENGTH_LONG);
		eventList = events;
		return events;
	}

	ArrayList<RatingResultItem> jsonRatingToArrayLiast(JSONArray eventsJsonArray) {
		ArrayList<RatingResultItem> ratings = new ArrayList<RatingResultItem>();
		for (int i = 1; i < eventsJsonArray.length(); i = i + 2) {
			try {
				JSONObject eventJsonObject = (JSONObject) eventsJsonArray
						.get(i);
				RatingResultItem event = new RatingResultItem(
						eventJsonObject.getDouble("rating"),
						eventJsonObject.getInt("rating_count"));
				ratings.add(event);
			} catch (JSONException e) {
				Methods.makeToast(getActivity(), e.toString(),
						Toast.LENGTH_LONG);
			}
		}
		// Methods.makeToast(CreatorEventsActivity.this, ratings.size() + "",
		// Toast.LENGTH_LONG);
		ratingList = ratings;
		return ratings;
	}
	
	@Override
	public void onResume() {
		makeVolleyRequest(listView, progressBar);
		super.onResume();
	}
}
