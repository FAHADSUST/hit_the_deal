package adapter;

import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fahad.ornob.sust.hitthedeal.R;

import customviews.FeedImageView;
import databaseEntities.Creator1;
import databaseEntities.Event;
import databaseEntities.RatingResultItem;

public class FeedListAdapter extends BaseAdapter {
	private final Activity activity;
	private LayoutInflater inflater;
	private final List<Event> eventItems;
	private final Creator1 creator;
	private final ArrayList<RatingResultItem> ratingResultItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public FeedListAdapter(Activity activity, List<Event> eventItems,
			Creator1 creator, ArrayList<RatingResultItem> ratingResultItems) {
		this.activity = activity;
		this.eventItems = eventItems;
		this.creator = creator;
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
			convertView = inflater.inflate(R.layout.feed_item_ornob, null);

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
		feedImageView.setDefaultImageResId(R.drawable.event_default);
		
		Event eventItem = eventItems.get(position);
		// UserItem userItem = userItems.get(position);
		RatingResultItem ratingResultItem = ratingResultItems.get(position);

		name.setText(eventItem.getEventName());
		orgName.setText(creator.getUserName());

		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				eventItem.getStartDate(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		startTimestamp.setText(timeAgo);

		CharSequence timeAgo2 = DateUtils.getRelativeTimeSpanString(
				eventItem.getEndDate(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		endTimestamp.setText(timeAgo2);

		setRatingbar.setRating((float) ratingResultItem.getRating());
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
		if (eventItem.getEventUrl() != null) {
			url.setText(Html.fromHtml("<a href=\"" + eventItem.getEventUrl()
					+ "\">" + eventItem.getEventUrl() + "</a> "));

			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);
		} else {
			// url is null, remove from the view
			url.setVisibility(View.GONE);
		}

		// user profile pic
		profilePic.setImageUrl(Constants.urlgetImgServlet+creator.getImageUrl(), imageLoader);

		// Feed image
		if (eventItem.getEventImg() != null) {
			feedImageView.setImageUrl(Constants.urlgetImgServlet+eventItem.getEventImg(), imageLoader);
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

		return convertView;
	}

}
