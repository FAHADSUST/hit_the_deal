package com.fahad.ornob.sust.hitthedeal.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fahad.ornob.sust.hitthedeal.EventDetailActivity;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.ViwerActivity;
import com.fahad.ornob.sust.hitthedeal.adapter.FeedListAdapter;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.RatingResultItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public class AllEventsFragment extends Fragment {

	private static final String TAG = ViwerActivity.class.getSimpleName();
	private PullToRefreshListView listView;
	private FeedListAdapter listAdapter;
	private List<Event> eventItems;
	private ArrayList<UserItem> userItems;

	private ArrayList<RatingResultItem> ratingResultItems;
	ConnectionDetector cd;

	// ////////////////////barti///////////////////////
	private LinkedList<Event> eventItemsLink;
	private LinkedList<UserItem> userItemsLink;
	private LinkedList<RatingResultItem> ratingResultItemsList;

	private LinkedList<String> mListItems;
	private ArrayAdapter<String> mAdapter;
	ListView actualListView;
	int start_index;
	int end_index;
	View footerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_all_events,
				container, false);

		cd = new ConnectionDetector(getActivity());

		eventItems = new ArrayList<Event>();
		userItems = new ArrayList<UserItem>();
		ratingResultItems = new ArrayList<RatingResultItem>();

		// //////////////////barti////////////////
		eventItemsLink = new LinkedList<Event>();
		userItemsLink = new LinkedList<UserItem>();
		ratingResultItemsList = new LinkedList<RatingResultItem>();

		Toast.makeText(getActivity(), "Halum halum", Toast.LENGTH_SHORT).show();

		listView = (PullToRefreshListView) rootView
				.findViewById(R.id.list_all_events);

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				GetDataTask(0,0);
			}

		});

		// Add an end-of-list listener
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				
				
				GetDataTask(end_index,1);
				
			}
		});

		actualListView = listView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		listAdapter = new FeedListAdapter(getActivity(), eventItems, userItems,
				ratingResultItems);

		actualListView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Event eventItem = eventItems.get(position - 1);
				Toast.makeText(
						getActivity(),
						"position:" + position + " , id: "
								+ eventItem.getEventId(), Toast.LENGTH_SHORT)
						.show();

				Intent intent = new Intent(getActivity(),
						EventDetailActivity.class);
				intent.putExtra("selectedEventId", eventItem.getEventId());
				startActivity(intent);
			}
		});

		if(start_index==0){
			GetDataTask(0,0);
		}
		footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_footer_loading, null, false);
		actualListView.addFooterView(footerView);
			

		return rootView;

	}
	
	private void GetDataTask(int start_index,final int typeUpOrFoter) {
		Toast.makeText(getActivity(), "Lkoadind: start: "+start_index+" : "+typeUpOrFoter , Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub
		String url=Constants.urlGetAllEvent+"?"+"index="+start_index;
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(url);

		if (cd.isConnectingToInternet()) {
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					url, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								if(typeUpOrFoter==0)
									parseJsonFeed(response, -1);
								else if(typeUpOrFoter==1)parseJsonFeedFooter(response, typeUpOrFoter);
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

	private void parseJsonFeed(JSONObject response, int itemType) {
		try {

			int success = response.getInt(DataBaseKeys.Success);
			if (success == 1) {
				
				eventItems.clear();
				eventItemsLink.clear();
				userItems.clear();
				userItemsLink.clear();
				ratingResultItems.clear();
				ratingResultItemsList.clear();
				
				JSONArray eventsJsonArray = response.getJSONArray("all");

				for (int i = 0; i < eventsJsonArray.length(); i++) {
					JSONObject eventJsonObject = (JSONObject) eventsJsonArray
							.get(i);

					Event event = new Event(
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

					eventItemsLink.addLast(event);

					UserItem userItem = new UserItem(
							eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
							eventJsonObject.getString(DataBaseKeys.USER_NAME),
							eventJsonObject
									.getString(DataBaseKeys.USER_IMAGE_URL),
							eventJsonObject
									.getInt(DataBaseKeys.USER_CREATOR_TYPE_ID));
					userItemsLink.addLast(userItem);

					JSONObject ratingJsonObject = eventJsonObject
							.getJSONObject(DataBaseKeys.RatingDetailTag);
					RatingResultItem ratingResultItem = new RatingResultItem(
							ratingJsonObject.getDouble(DataBaseKeys.RatingTag),
							ratingJsonObject
									.getInt(DataBaseKeys.RatingCounNumberTag));
					ratingResultItemsList.addLast(ratingResultItem);
					
					end_index = event.getEventId();

				}

				
				eventItems.addAll(eventItemsLink);
				userItems.addAll(userItemsLink);
				ratingResultItems.addAll(ratingResultItemsList);

				listAdapter = new FeedListAdapter(getActivity(), eventItems,
						userItems, ratingResultItems);
				actualListView.setAdapter(listAdapter);
				listAdapter.notifyDataSetChanged();

				listView.onRefreshComplete();
				
				//start_index=eventItems.get(eventItems.size()-1).getEventId();

			} else {
				Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT)
						.show();
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	private void parseJsonFeedFooter(JSONObject response, int itemType) {
		try {
			
			int success = response.getInt(DataBaseKeys.Success);
			if (success == 1) {
				eventItemsLink.clear();
				userItemsLink.clear();
				ratingResultItemsList.clear();
				
				JSONArray eventsJsonArray = response.getJSONArray("all");

				for (int i = 0; i < eventsJsonArray.length(); i++) {
					JSONObject eventJsonObject = (JSONObject) eventsJsonArray
							.get(i);

					Event event = new Event(
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

					eventItemsLink.addFirst(event);

					UserItem userItem = new UserItem(
							eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
							eventJsonObject.getString(DataBaseKeys.USER_NAME),
							eventJsonObject
									.getString(DataBaseKeys.USER_IMAGE_URL),
							eventJsonObject
									.getInt(DataBaseKeys.USER_CREATOR_TYPE_ID));
					userItemsLink.addFirst(userItem);

					JSONObject ratingJsonObject = eventJsonObject
							.getJSONObject(DataBaseKeys.RatingDetailTag);
					RatingResultItem ratingResultItem = new RatingResultItem(
							ratingJsonObject.getDouble(DataBaseKeys.RatingTag),
							ratingJsonObject
									.getInt(DataBaseKeys.RatingCounNumberTag));
					ratingResultItemsList.addFirst(ratingResultItem);
					
					end_index=event.getEventId();

				}

				
//				
//				eventItems.addAll(eventItemsLink);
//				userItems.addAll(userItemsLink);
//				ratingResultItems.addAll(ratingResultItemsList);
				
				for(int i=0;i<eventItemsLink.size();i++){
					int index=0;
					boolean bool=true;
					for(int j=0;j<eventItems.size();j++){
						
						if(eventItems.get(j).getEventId()==eventItemsLink.get(i).getEventId()){
							bool=false;
							break;
						}
						index++;
					}
					if(bool){
						eventItems.add(eventItemsLink.get(i));
						userItems.add(userItemsLink.get(i));
						ratingResultItems.add(ratingResultItemsList.get(i));
					}else{
						eventItems.set(index,eventItemsLink.get(i));
						userItems.set(index,userItemsLink.get(i));
						ratingResultItems.set(index,ratingResultItemsList.get(i));
					}
				}

				listAdapter.notifyDataSetChanged();

				
				
				//start_index=eventItems.get(eventItems.size()-1).getEventId();

			} else {
				Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT)
						.show();
				
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

}