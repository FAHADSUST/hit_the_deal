package com.fahad.ornob.sust.hitthedeal.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.fahad.ornob.sust.hitthedeal.adapter.MyCreatorListViewAdapter;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.RatingResultItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class MyFavCreatorFragment  extends Fragment {
	
	private static final String TAG = MyFavCreatorFragment.class.getSimpleName();
	private ListView listView;
	private MyCreatorListViewAdapter listAdapter;
	private ArrayList<UserItem> userItems;
	ConnectionDetector cd;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.myfavorite_creator_fragment, container, false);
		
		cd = new ConnectionDetector(getActivity());
		
		userItems = new ArrayList<UserItem>();
		
		listView = (ListView) rootView.findViewById(R.id.myCreatorListProfileFrgListView);
	
	
		listAdapter = new MyCreatorListViewAdapter(getActivity(), userItems);
		listView.setAdapter(listAdapter);
		
		
		GetDataTask(Constants.urlGetMyFavCreator+"viewer_id="+Constants.userItem.getUser_id());

						
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Event eventItem = eventItems.get(position-1);
				Toast.makeText(getActivity(), "position:"+position+" , id: ", Toast.LENGTH_SHORT).show();
				/*
			    Intent intent = new Intent(getActivity(),EventDetailActivity.class);
			    intent.putExtra("selectedEventId", eventItem.getEventId());
			    startActivity(intent);*/
			}
		});
		
		
		
		
		return rootView;

	}

	
	
	private void GetDataTask(String url) {
		// TODO Auto-generated method stub
	
		
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(url);
		
		if(cd.isConnectingToInternet()){
			Toast.makeText(getActivity(),"sdsd", Toast.LENGTH_SHORT).show();
			
			
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
					
					listAdapter = new MyCreatorListViewAdapter(getActivity(), userItems);
					listView.setAdapter(listAdapter);
					listAdapter.notifyDataSetChanged();					
				 
			 }else{
				 Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
			 }
			
		} catch (JSONException e) {

			
			e.printStackTrace();
		}
		
	}
}
