package com.fahad.ornob.sust.hitthedeal.fragment;




import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.ViwerActivity;
import com.fahad.ornob.sust.hitthedeal.adapter.FeedListAdapter;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class AroundMeFragment extends Fragment {

	private static final String TAG = ViwerActivity.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<Event> eventItems;
	private ArrayList<UserItem> userItems;
	ConnectionDetector cd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
		
		cd = new ConnectionDetector(getActivity());
		listView = (ListView) rootView.findViewById(R.id.list);

		eventItems = new ArrayList<Event>();
		userItems = new ArrayList<UserItem>();

		listAdapter = new FeedListAdapter(getActivity(), eventItems,userItems);
		listView.setAdapter(listAdapter);
		
		
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(Constants.urlGetAllEvent);
		
		if(cd.isConnectingToInternet()){
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					Constants.urlGetAllEvent, null, new Response.Listener<JSONObject>() {

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
		
		return rootView;

	}

	
	
	private  void parseJsonFeed(JSONObject response,int itemType) {
		try {
			eventItems.clear();
			 int success = response.getInt(DataBaseKeys.Success);
			 if(success==1){
				 JSONArray eventsJsonArray = response.getJSONArray("all");

					for (int i = 0; i < eventsJsonArray.length(); i++) {
						JSONObject eventJsonObject = (JSONObject) eventsJsonArray.get(i);

						Event event = new Event(
								eventJsonObject.getInt(DataBaseKeys.EVENT_ID),
								eventJsonObject.getInt(DataBaseKeys.CREATOR_ID),
								eventJsonObject
										.getString(DataBaseKeys.EVENT_DESCRIPTIOPN),
								eventJsonObject.getLong(DataBaseKeys.START_DATE),
								eventJsonObject.getLong(DataBaseKeys.END_DATE),
								eventJsonObject.getDouble(DataBaseKeys.LATITUDE),
								eventJsonObject.getDouble(DataBaseKeys.LONGITUDE),
								eventJsonObject
								.getString(DataBaseKeys.EVENT_IMG),
								eventJsonObject
								.getString(DataBaseKeys.EVENT_URL));
						
						eventItems.add(event);
						
						UserItem userItem = new UserItem(eventJsonObject.getInt(DataBaseKeys.CREATOR_ID), eventJsonObject.getString(DataBaseKeys.USER_NAME), eventJsonObject.getString(DataBaseKeys.USER_IMAGE_URL), eventJsonObject.getInt(DataBaseKeys.USER_CREATOR_TYPE_ID));
						userItems.add(userItem);
					}
					
					listAdapter.notifyDataSetChanged();
				 
			 }else{
				 Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
			 }
			
		} catch (JSONException e) {

			e.printStackTrace();
		}
		
	}

	
}