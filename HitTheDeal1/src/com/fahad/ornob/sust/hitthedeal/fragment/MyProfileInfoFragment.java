package com.fahad.ornob.sust.hitthedeal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;

public class MyProfileInfoFragment  extends Fragment {
	
	TextView emailMyProfileFrgTxt,addressMyProfileFrgTxt,phoneMyProfileFrgTxt,dateCreatMyProfileFrgTxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.my_profile_info_fragment, container, false);
		
		emailMyProfileFrgTxt =(TextView) rootView.findViewById(R.id.emailMyProfileFrgTxt); 
		addressMyProfileFrgTxt =(TextView) rootView.findViewById(R.id.addressMyProfileFrgTxt); 
		phoneMyProfileFrgTxt =(TextView) rootView.findViewById(R.id.phoneMyProfileFrgTxt); 
		dateCreatMyProfileFrgTxt=(TextView) rootView.findViewById(R.id.dateCreatMyProfileFrgTxt);
		
		emailMyProfileFrgTxt.setText(Constants.userItem.getEmail() ); 
		if(!Constants.userItem.getAddress().isEmpty()){
			addressMyProfileFrgTxt.setText(Constants.userItem.getAddress() ); 
		}else addressMyProfileFrgTxt.setText("Please add your address.");
		if(!Constants.userItem.getPhn_no().isEmpty())
			phoneMyProfileFrgTxt.setText(Constants.userItem.getPhn_no() );
		else phoneMyProfileFrgTxt.setText("Please add your phone number." );
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Constants.userItem.getDate_of_creation() ,
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		dateCreatMyProfileFrgTxt.setText(timeAgo);
		
		return rootView;
	}
}