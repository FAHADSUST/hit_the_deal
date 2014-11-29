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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fahad.ornob.sust.hitthedeal.FeedImageView;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.FeedBackItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;

public class EventFeedBackAdapter  extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<FeedBackItem> feedBackItems;
	private ArrayList<UserItem> userItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public EventFeedBackAdapter(Activity activity, ArrayList<FeedBackItem> feedBackItems, ArrayList<UserItem> userItems) {
		this.activity = activity;
		this.feedBackItems = feedBackItems;
		this.userItems = userItems;
	}

	@Override
	public int getCount() {
		return feedBackItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedBackItems.get(location);
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
			convertView = inflater.inflate(R.layout.event_detai_feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.feedBackUserName);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestampFeedback);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.feedbackTxt);
		
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.visitorProPic);
		//profilePic.setDefaultImageResId(R.drawable.ic_launcher);
		

		FeedBackItem feedBackItem = feedBackItems.get(position);
		UserItem userItem = userItems.get(position);
		
		name.setText(userItem.getUser_name());

		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				feedBackItem.getDate(),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);

		// Chcek for empty status message
		if (!TextUtils.isEmpty(feedBackItem.getFeedback())) {
			statusMsg.setText(feedBackItem.getFeedback());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		profilePic.setImageUrl(Constants.urlgetImgServlet+userItem.getImage_url(), imageLoader);

		// Feed image
		

		return convertView;
	}

}