package fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.fahad.ornob.sust.hitthedeal.CreateEventActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.CreatorActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.EditCreatorProfileActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.faizmalkani.floatingactionbutton.FloatingActionButton;

@SuppressLint("NewApi")
public class FragmentCreatorAbout extends Fragment {

	Animation slideUp, slideDown;
	LinearLayout slidingLayout;
	FrameLayout frameLayout;
	TextView nameTv, addressTv, phnTv, emailTv, typeTv;
	Button createEventButton;
	FloatingActionButton floatingActionButton;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about_creator, null);

		slideUp = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_up);
		slideDown = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_down);

		slidingLayout = (LinearLayout) view.findViewById(R.id.sliding_layout);
		slidingLayout.setAlpha(0.90f);

		frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
		frameLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				slideButton();
				return false;
			}
		});

		nameTv = (TextView) view.findViewById(R.id.nameTv);
		addressTv = (TextView) view.findViewById(R.id.addressTv);
		phnTv = (TextView) view.findViewById(R.id.phnTv);
		emailTv = (TextView) view.findViewById(R.id.emailTv);
		typeTv = (TextView) view.findViewById(R.id.creator_typeTv);

		createEventButton = (Button) view
				.findViewById(R.id.create_event_button);
		createEventButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						CreateEventActivityOrnob.class);
				startActivity(intent);
			}
		});

		floatingActionButton = (FloatingActionButton) view
				.findViewById(R.id.floating_button);
		floatingActionButton.setColor(Color.parseColor("#FF9500"));
		floatingActionButton.setDrawable(getResources().getDrawable(
				R.drawable.floating_button_icon));
		floatingActionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						EditCreatorProfileActivityOrnob.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("creator", CreatorActivityOrnob.creator);
				startActivity(intent);
			}
		});

		if(imageLoader == null){
			imageLoader = AppController.getInstance().getImageLoader();
		}
		
		updateProfieViews();

		return view;
	}

	
	void updateProfieViews() {
		nameTv.setText(CreatorActivityOrnob.creator.getUserName());
		addressTv.setText("  " + CreatorActivityOrnob.creator.getAddress());
		phnTv.setText("  " + CreatorActivityOrnob.creator.getPhnNo());
		emailTv.setText("  " + CreatorActivityOrnob.creator.getEmail());
		typeTv.setText("  " + CreatorActivityOrnob.creator.getCreatorType() + "  ");
		
		CreatorActivityOrnob.profileImageView.setImageUrl(Constants.urlgetImgServlet+CreatorActivityOrnob.creator.getImageUrl(), imageLoader);
	}

	void slideButton() {
		if (slidingLayout.getVisibility() == View.INVISIBLE) {
			slidingLayout.setVisibility(View.VISIBLE);
			slidingLayout.startAnimation(slideUp);
		} else {
			slidingLayout.startAnimation(slideDown);
			slidingLayout.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onResume() {
		updateProfieViews();
		super.onResume();
	}
}
