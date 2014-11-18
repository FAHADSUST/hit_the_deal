package com.fahad.ornob.sust.hitthedeal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.CreatorTypeItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpCreatorPage extends Activity{
	
	private static final String TAG = SignUpCreatorPage.class.getSimpleName();
	private static final int INITIAL_DELAY_MILLIS = 300;
	ConnectionDetector cd;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_page_main);
		show(0);
		initCreatorSignUp();
		initMap();
        listenerCreatorSignUp();
		
	}
	
	//*******************map shuru******************************\\
	private void show(int i) {
		// TODO Auto-generated method stub
		if(i==0){
			findViewById(R.id.sign_up_creator_id).setVisibility(View.VISIBLE);
			findViewById(R.id.map_fragment_layout_id).setVisibility(View.GONE);
		}else if(i==1){
			findViewById(R.id.sign_up_creator_id).setVisibility(View.GONE);
			findViewById(R.id.map_fragment_layout_id).setVisibility(View.VISIBLE);
		}
	}
	
	
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	Location myLocation;
	TextView tvLocInfo;
	Marker marker = null;
	Double donor_latitude;
	Double donor_longitude;	
	private void initMap() {
		// TODO Auto-generated method stub
		tvLocInfo = (TextView) findViewById(R.id.tv_location);
		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment) myFragmentManager
				.findFragmentById(R.id.map);
		myMap = myMapFragment.getMap();
		myMap.setMyLocationEnabled(true);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		myLocation();					
			myMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				
				@Override
				public void onMapLongClick(LatLng point) {
					// TODO Auto-generated method stub
					if (marker != null) {
						marker.remove();
					}
					marker = myMap.addMarker(new MarkerOptions().position(point)
							.draggable(true).visible(true));

					donor_latitude = point.latitude;
					donor_longitude = point.longitude;

					new AlertDialog.Builder(SignUpCreatorPage.this)
							.setTitle("Setting Position")
							.setMessage(
									"Are you sure you want to set this location as your fixed position ?")
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											
											show(0);
										}
									})
							.setNegativeButton(android.R.string.no,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											// do nothing

											marker.remove();
										}
									}).show();
				}
			});
	}

	private void myLocation() {
		// TODO Auto-generated method stub

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		String provider = locationManager.getBestProvider(criteria, true);
		Location myLocation = locationManager.getLastKnownLocation(provider);

		double latitude = myLocation.getLatitude();
		double longitude = myLocation.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		MarkerOptions marker = new MarkerOptions().position(latLng).title(
				"My Location!");
		myMap.addMarker(marker);

		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {
			Toast.makeText(getApplicationContext(),
					"isGooglePlayServicesAvailable SUCCESS", Toast.LENGTH_LONG)
					.show();
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					RQS_GooglePlayServices);
		}

	}
	//*******************map------------ shesh******************************\\
	

    ImageView orgImgView;
	EditText creatorOrgNameEd,creatorEmailEd,creatorAddressEd,creatorPassEd;
    Button captureCretImgB,browseCreateImgB,creatorSignUpB;
    ImageButton creatorAddLocationB;
    Spinner creatorTypeSpin;
    private String[] state = { "local_business", "reataurant", "cause", "cultural", "Other"};
    ArrayList<CreatorTypeItem> creatorTypeItems;
    
	private void initCreatorSignUp() {
		// TODO Auto-generated method stub
		orgImgView=(ImageView) findViewById(R.id.organizationImgView);
		
		creatorOrgNameEd=(EditText) findViewById(R.id.orgNameCreatorSignUpEd); 
		creatorEmailEd=(EditText) findViewById(R.id.emailCreatSignEd); 
		creatorAddressEd=(EditText) findViewById(R.id.addressCreatSignCEd); 
		creatorPassEd=(EditText) findViewById(R.id.passCreatSignEd); 
		
		captureCretImgB=(Button) findViewById(R.id.captureCreateImgB);
		browseCreateImgB=(Button) findViewById(R.id.browseCreatImgB);
		creatorSignUpB=(Button) findViewById(R.id.signupCreatorB);
		creatorAddLocationB=(ImageButton) findViewById(R.id.addLocationCreatorB);		
		
		creatorTypeSpin = (Spinner) findViewById(R.id.creatorTypeIdSpin);
		
		creatorTypeItems = new ArrayList<CreatorTypeItem>();
		
		  ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
		           android.R.layout.simple_spinner_item, state);
		 adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 creatorTypeSpin.setAdapter(adapter_state);
		 
		 //jsonUniAsync(Constants.urlCreatorType, Constants.CreatorTypeItem);
		 
		 
		 
			
		
	}
	
	private void listenerCreatorSignUp() {
		// TODO Auto-generated method stub
    	captureCretImgB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	browseCreateImgB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
    	
    	creatorSignUpB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*YoYo.with(Techniques.SlideInUp)
		        .duration(700)
		        .playOn(findViewById(R.id.fbLoginRA));*/
			}
		});
    	creatorAddLocationB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*YoYo.with(Techniques.SlideInUp)
		        .duration(700)
		        .playOn(findViewById(R.id.fbLoginRA));*/
				show(1);
				//myLocation();*/
				
				//Intent intent = new Intent(SignUpCreatorPage.this,GetMapCreatorLocationActivity.class);
				//startActivity(intent);
			}

			
		});
    	creatorTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
			          long id) {
				// TODO Auto-generated method stub
				 creatorTypeSpin.setSelection(position);
				 String catagory = String.valueOf(position);
		         String selState = (String) creatorTypeSpin.getSelectedItem();
		         Toast.makeText(SignUpCreatorPage.this, catagory, Toast.LENGTH_SHORT).show();
		         //selVersion.setText("Selected Android OS:" + selState);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	
	}
	
	private void jsonUniAsync(String url,final int itemType) {
		
    	cd = new ConnectionDetector(this);
    	if(!cd.isConnectingToInternet()){
			Cache cache = AppController.getInstance().getRequestQueue().getCache();
			Entry entry = cache.get(url);
			if (entry != null) {
				// fetch the data from cache
				try {
					String data = new String(entry.data, "UTF-8");
					try {
						parseJsonFeed(new JSONObject(data),itemType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		} else {
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response,itemType);
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
		}
		
	}
	
	private void parseJsonFeed(JSONObject response,int itemType) {
		try {
			
			 int success = response.getInt(DataBaseKeys.Success);
			 if(success==1){
				 if(itemType==Constants.loginType){
					 int userTypeID = response.getInt(DataBaseKeys.user_type_id);
					 if(userTypeID==Constants.Creator){
						 Toast.makeText(this, "Creator", Toast.LENGTH_SHORT).show();
					 }else{
						 Toast.makeText(this, "Visitor", Toast.LENGTH_SHORT).show();
					 }
				 }else if(itemType==Constants.CreatorTypeItem){
					 
					 JSONArray feedArray = response.getJSONArray("all");
					 	
					 	creatorTypeItems.clear();
						for (int i = 0; i < feedArray.length(); i++) {
							JSONObject feedObj = (JSONObject) feedArray.get(i);
							
							CreatorTypeItem item = new CreatorTypeItem(feedObj.getInt(DataBaseKeys.creatorTypekey[0]),feedObj.getString(DataBaseKeys.creatorTypekey[1]));							
							creatorTypeItems.add(item);
						}
						
				 }
			 }else{
				 Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
			 }
			
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
	
	
	public boolean showWarningDialog(String email,String pass){
		
		/*if(.getText().toString().isEmpty()){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnUserName, false);
    		
		}else */
		if(email.isEmpty()){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnEmail, false);
    		
		}else if(pass.isEmpty()){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnPass, false);
    		
		}else if(!validEmail(email)){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnEmailPatern, false);
    		
		}else{
			return true;
		}
		return false;
	}
	
	private boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
}
