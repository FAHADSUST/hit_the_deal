package com.fahad.ornob.sust.hitthedeal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.contants.FileUpload;
import com.fahad.ornob.sust.hitthedeal.item.CreatorTypeItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.dp;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
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

public class SignUpCreatorPage extends Activity {

	private static final String TAG = SignUpCreatorPage.class.getSimpleName();
	private static final int INITIAL_DELAY_MILLIS = 300;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_page_main);
		show(Constants.MapViewGone);
		initCreatorSignUp();
		initMap();
		listenerCreatorSignUp();

	}

	// *******************map shuru******************************\\
	private void show(int i) {
		// TODO Auto-generated method stub
		if (i == Constants.MapViewGone) {
			// findViewById(R.id.sign_up_creator_id).setVisibility(View.VISIBLE);
			findViewById(R.id.map_fragment_layout_id).setVisibility(View.GONE);
		} else if (i == Constants.MapViewShow) {
			// findViewById(R.id.sign_up_creator_id).setVisibility(View.GONE);
			findViewById(R.id.map_fragment_layout_id).setVisibility(
					View.VISIBLE);
		}
	}

	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	Location myLocation;
	Button closeMapB;
	Marker marker = null;
	Double donor_latitude;
	Double donor_longitude;

	private void initMap() {
		// TODO Auto-generated method stub
		closeMapB = (Button) findViewById(R.id.closeB);
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

										YoYo.with(Techniques.SlideOutRight)
												.duration(700)
												.playOn(findViewById(R.id.map_fragment_layout_id));

										// show(Constants.MapViewGone);
										//Toast.makeText(SignUpCreatorPage.this, donor_latitude+":"+donor_longitude, Toast.LENGTH_SHORT).show();

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
		closeMapB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YoYo.with(Techniques.SlideOutRight)
				.duration(700)
				.playOn(findViewById(R.id.map_fragment_layout_id));

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
			/*
			 * Toast.makeText(getApplicationContext(),
			 * "isGooglePlayServicesAvailable SUCCESS", Toast.LENGTH_LONG)
			 * .show();
			 */
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					RQS_GooglePlayServices);
		}

	}

	// *******************map------------ shesh******************************\\
	private static final int CAMERA_REQUEST = 1888;
	private static final int FILE_SELECT_CODE = 0;
	ImageView orgImgView;
	EditText creatorOrgNameEd, creatorEmailEd, creatorAddressEd, creatorPassEd;
	Button captureCretImgB, browseCreateImgB, creatorSignUpB;
	ImageButton creatorAddLocationB;
	Spinner creatorTypeSpin;
	private String[] state = { "local_business", "reataurant", "cause",
			"cultural", "Other" };
	ArrayList<CreatorTypeItem> creatorTypeItems;

	private void initCreatorSignUp() {
		// TODO Auto-generated method stub
		orgImgView = (ImageView) findViewById(R.id.organizationImgView);

		creatorOrgNameEd = (EditText) findViewById(R.id.orgNameCreatorSignUpEd);
		creatorEmailEd = (EditText) findViewById(R.id.emailCreatSignEd);
		creatorAddressEd = (EditText) findViewById(R.id.addressCreatSignCEd);
		creatorPassEd = (EditText) findViewById(R.id.passCreatSignEd);

		captureCretImgB = (Button) findViewById(R.id.captureCreateImgB);
		browseCreateImgB = (Button) findViewById(R.id.browseCreatImgB);
		creatorSignUpB = (Button) findViewById(R.id.signupCreatorB);
		creatorAddLocationB = (ImageButton) findViewById(R.id.addLocationCreatorB);

		creatorTypeSpin = (Spinner) findViewById(R.id.creatorTypeIdSpin);

		creatorTypeItems = new ArrayList<CreatorTypeItem>();

		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, state);
		adapter_state
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		creatorTypeSpin.setAdapter(adapter_state);

		// jsonUniAsync(Constants.urlCreatorType, Constants.CreatorTypeItem);

	}

	File image;	
	Uri ImageUri;		
	public static String imageFileName=null;

	private void listenerCreatorSignUp() {
		// TODO Auto-generated method stub
		captureCretImgB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				image = FileUpload.getFileForCaptureCreatorSignUp(Constants.CreatorTypeID);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(image));
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		browseCreateImgB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);

				try {
					startActivityForResult(Intent.createChooser(intent,
							"Select a File to Upload"), FILE_SELECT_CODE);
				} catch (android.content.ActivityNotFoundException ex) {
					// Potentially direct the user to the Market with a Dialog
					Toast.makeText(getApplicationContext(),
							"Please install a File Manager.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		creatorSignUpB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				String renameStr = null;
				
				if (imageFileName != null ) {
					renameStr = creatorEmailEd.getText().toString()
							+ ".jpg";															
				}else{
					renameStr=Constants.ImgEmtyTAG;
				}
				if(showWarningDialog()){
					if(imageFileName != null ){
						FileUpload.uploadFileGetInstance(SignUpCreatorPage.this,imageFileName,renameStr);
					}
					String urlCreatorSignUp=Constants.urlInsertSignUpData+"user_type_id=1&user_name="+creatorOrgNameEd.getText().toString()+"&address="+creatorAddressEd.getText().toString()+"&email="+creatorEmailEd.getText().toString()+"&phn_no="+"211"+"&date_of_creation="+CommonMethod.currentTimeFrom1970()+"&latitude="+donor_latitude+"&longitude="+donor_longitude+"&image_url="+renameStr+"&password="+creatorPassEd.getText().toString()+"&creator_type_id="+creatorTypeSpin.getSelectedItemPosition()+1;					
					Log.e(TAG, "Response: " + urlCreatorSignUp);
					jsonUniAsync(urlCreatorSignUp, Constants.CreatorSignUp);															
				}
				
			}
		});
		creatorAddLocationB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				show(Constants.MapViewShow);
				YoYo.with(Techniques.SlideInRight).duration(700)
						.playOn(findViewById(R.id.map_fragment_layout_id));

			}

		});
		creatorTypeSpin
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						creatorTypeSpin.setSelection(position);
						String catagory = String.valueOf(position);
						String selState = (String) creatorTypeSpin
								.getSelectedItem();
						Toast.makeText(SignUpCreatorPage.this, catagory,
								Toast.LENGTH_SHORT).show();
						// selVersion.setText("Selected Android OS:" +
						// selState);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			try {  
		          Bitmap captureBmp;
		          captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(image) );
		          ImageUri = Uri.fromFile(image);
		          orgImgView.setImageBitmap(captureBmp);
		          
		        } catch (FileNotFoundException e) {  
		          e.printStackTrace();  
		        } catch (IOException e) {  
		          e.printStackTrace();  
		        }

		} else if (resultCode == RESULT_OK) {

			Uri selectedImage = intent.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
			orgImgView.setImageBitmap(bitmap);

			File destinationFile = FileUpload.getFileForCaptureCreatorSignUp(Constants.CreatorTypeID);
			try {
				destinationFile = FileUpload.copyFile(new File(picturePath), destinationFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void jsonUniAsync(String url, final int itemType) {

		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			cd.showAlertDialogToNetworkConnection(this, "Connection loss", "No network connection.", false);

		} else {
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET, url,
					null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response, itemType);
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

	private void parseJsonFeed(JSONObject response, int itemType) {
		try {

			int success = response.getInt(DataBaseKeys.Success);
			if (success == 1) {
				if (itemType == Constants.loginType) {
					int userTypeID = response.getInt(DataBaseKeys.user_type_id);
					if (userTypeID == Constants.Creator) {
						Toast.makeText(this, "Creator", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(this, "Visitor", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (itemType == Constants.CreatorTypeItem) {

					JSONArray feedArray = response.getJSONArray("all");

					creatorTypeItems.clear();
					for (int i = 0; i < feedArray.length(); i++) {
						JSONObject feedObj = (JSONObject) feedArray.get(i);

						CreatorTypeItem item = new CreatorTypeItem(
								feedObj.getInt(DataBaseKeys.creatorTypekey[0]),
								feedObj.getString(DataBaseKeys.creatorTypekey[1]));
						creatorTypeItems.add(item);
					}

				} else if (itemType == Constants.CreatorSignUp) {
					
						Toast.makeText(this, "Suucess", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(SignUpCreatorPage.this,CreatorActivity.class);
						startActivity(intent);
						finish();
				}

			} else {
				Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	public boolean showWarningDialog() {

		if (creatorOrgNameEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnOrgName, false);

		} else if (creatorEmailEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnEmail, false);

		} else if (creatorAddressEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnAddess, false);

		} else if (String.valueOf(donor_latitude).isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnLocation, false);
		  
		  }
		 
		else if (creatorPassEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnPass, false);

		} else if (!validEmail(creatorEmailEd.getText().toString())) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnEmailPatern, false);

		} else {
			return true;
		}
		return false;
	}

	private boolean validEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}
	
}
