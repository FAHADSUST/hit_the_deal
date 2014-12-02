package com.fahad.ornob.sust.hitthedeal.adapter;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fahad.ornob.sust.hitthedeal.FeedImageView;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.RatingResultItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;

import databaseEntities.Creator1;

public class FeedListAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<Event> eventItems;
	private ArrayList<UserItem> userItems;
	private ArrayList<RatingResultItem> ratingResultItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public FeedListAdapter(Activity activity, List<Event> eventItems, ArrayList<UserItem> userItems, ArrayList<RatingResultItem> ratingResultItems) {
		this.activity = activity;
		this.eventItems = eventItems;
		this.userItems = userItems;
		this.ratingResultItems = ratingResultItems;
	}

	@Override
	public int getCount() {
		return eventItems.size();
	}

	@Override
	public Object getItem(int location) {
		return eventItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		
		TextView orgName = (TextView) convertView.findViewById(R.id.orgName);
		
		TextView startTimestamp = (TextView) convertView
				.findViewById(R.id.startTimestamp);
		
		TextView endTimestamp = (TextView) convertView
				.findViewById(R.id.endTimestamp);
		RatingBar setRatingbar = (RatingBar) convertView
				.findViewById(R.id.setRatingFeedItemBar);
		
		TextView setRatingBarTxt = (TextView) convertView
				.findViewById(R.id.setRatingFeedItemTxt);
		
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.profilePic);
		profilePic.setDefaultImageResId(R.drawable.default_profic);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);

		Event eventItem = eventItems.get(position);
		UserItem userItem = userItems.get(position);
		RatingResultItem ratingResultItem = ratingResultItems.get(position);
		
		name.setText(eventItem.getEvent_name());
		orgName.setText(userItem.getUser_name());

		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				eventItem.getStartDate(),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		startTimestamp.setText(timeAgo);
		
		CharSequence timeAgo2 = DateUtils.getRelativeTimeSpanString(
				eventItem.getEndDate(),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		endTimestamp.setText(timeAgo2);
		
		setRatingbar.setRating((float)ratingResultItem.getRating());
		setRatingBarTxt.setText(String.valueOf(ratingResultItem.getRating()));

		// Chcek for empty status message
		if (!TextUtils.isEmpty(eventItem.getEventDescription())) {
			statusMsg.setText(eventItem.getEventDescription());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url
		if (eventItem.getEvent_url() != null) {
			url.setText(Html.fromHtml("<a href=\"" + eventItem.getEvent_url() + "\">"
					+ eventItem.getEvent_url() + "</a> "));

			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);
		} else {
			// url is null, remove from the view
			url.setVisibility(View.GONE);
		}

		// user profile pic
		profilePic.setImageUrl(Constants.urlgetImgServlet+userItem.getImage_url(), imageLoader);

		// Feed image
		if (eventItem.getEvent_img() != null) {
			feedImageView.setImageUrl(Constants.urlgetImgServlet+eventItem.getEvent_img(), imageLoader);
			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
			feedImageView.setVisibility(View.GONE);
		}
		
		
		profilePic.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				return false;
			}
		});

		return convertView;
	}

}
