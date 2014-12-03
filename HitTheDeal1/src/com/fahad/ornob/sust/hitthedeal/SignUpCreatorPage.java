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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
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
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.CreatorTypeItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.dp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import databaseEntities.Creator1;

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
	ProgressDialog pDialog;

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
				.findFragmentById(R.id.mapCreatorSignUp);
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
										// Toast.makeText(SignUpCreatorPage.this,
										// donor_latitude+":"+donor_longitude,
										// Toast.LENGTH_SHORT).show();

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
				YoYo.with(Techniques.SlideOutRight).duration(700)
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
		MarkerOptions marker = new MarkerOptions().position(latLng).icon(
				BitmapDescriptorFactory
						.fromResource(R.drawable.icon_my_location));
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
	
	ImageButton captureCretImgB, browseCreateImgB;
	Button  creatorSignUpB;
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

		captureCretImgB = (ImageButton) findViewById(R.id.captureCreateImgB);
		browseCreateImgB = (ImageButton) findViewById(R.id.browseCreatImgB);
		creatorSignUpB = (Button) findViewById(R.id.signupCreatorB);
		creatorAddLocationB = (ImageButton) findViewById(R.id.addLocationCreatorB);

		creatorTypeSpin = (Spinner) findViewById(R.id.creatorTypeIdSpin);

		creatorTypeItems = new ArrayList<CreatorTypeItem>();

		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, state);
		adapter_state
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		creatorTypeSpin.setAdapter(adapter_state);

		
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Progressing...");
		pDialog.setCancelable(false);
		
		// jsonUniAsync(Constants.urlCreatorType, Constants.CreatorTypeItem);

	}

	File image;
	Uri ImageUri;
	String renameStr = null;

	private void listenerCreatorSignUp() {
		// TODO Auto-generated method stub
		captureCretImgB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				image = CommonMethod.getFileForCapture();
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(image));
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		browseCreateImgB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				try {
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_IMAGE);
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

				if (image != null) {
					renameStr = creatorEmailEd.getText().toString() + ".jpg";
				} else {
					renameStr = Constants.ImgEmtyTAG;
				}
				if (showWarningDialog()) {
					/*if (image != null) {
						CommonMethod cm = new CommonMethod();
						cm.uploadImage(SignUpCreatorPage.this, renameStr, image);
					}*/
					pDialog.show();
					jsonUniAsync(Constants.urlInsertSignUpData,
							Constants.CreatorSignUp);
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

	private static final int PICK_IMAGE = 1;

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			try {
				Bitmap captureBmp = Media.getBitmap(getContentResolver(),
						Uri.fromFile(image));
				Uri ImageUri = Uri.fromFile(image);
				orgImgView.setImageBitmap(captureBmp);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("Fahad", e.toString());
			}

		} else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE
				&& intent != null && intent.getData() != null) {
			Uri _uri = intent.getData();
			Cursor cursor = getContentResolver()
					.query(_uri,
							new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
							null, null, null);
			cursor.moveToFirst();

			final String imageFilePath = cursor.getString(0);
			cursor.close();

			orgImgView.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));

			image = new File(imageFilePath);
		}

	}

	private void jsonUniAsync(String url, final int itemType) {

		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			cd.showAlertDialogToNetworkConnection(this, "Connection loss",
					"No network connection.", false);

		} else {
			StringRequest jsonReq = new StringRequest(Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								
								parseJsonFeed(response, itemType);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
							pDialog.dismiss();
						}

					}) {

				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();

					params.put("user_type_id",String.valueOf(Constants.CreatorTypeID));
					params.put("user_name", creatorOrgNameEd.getText().toString());
					params.put("address", creatorAddressEd.getText().toString());
					params.put("email", creatorEmailEd.getText().toString());
					params.put("phn_no", "211");
					params.put("date_of_creation",
							String.valueOf(CommonMethod.currentTimeFrom1970()));
					params.put("latitude", String.valueOf(donor_latitude));
					params.put("longitude", String.valueOf(donor_longitude));
					params.put("image_url", renameStr);
					params.put("password", creatorPassEd.getText().toString());

					int creatorTypeId = creatorTypeSpin
							.getSelectedItemPosition() + 1;
					params.put("creator_type_id", String.valueOf(creatorTypeId));
					
					params.put("image_name", renameStr);//convertFileToString
					if(image!=null)
						params.put("image", convertFileToString(image));
					else params.put("image", "");

					return params;
				}
			};

			
			AppController.getInstance().addToRequestQueue(jsonReq);
		}

	}

	public void parseJsonFeed(String responseString, int itemType) {
		try {

			JSONObject response = new JSONObject(responseString);

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
					int user_id = response.getInt("user_id");
					
					int creatorTypeId = creatorTypeSpin
							.getSelectedItemPosition() + 1;
					UserItem userItem = new UserItem(user_id, Constants.CreatorTypeID, creatorOrgNameEd.getText().toString(), 
							creatorAddressEd.getText().toString(), creatorEmailEd.getText().toString(),
							"01675902585", CommonMethod.currentTimeFrom1970(), donor_latitude, donor_longitude, renameStr,
							creatorPassEd.getText().toString(), creatorTypeId);
						
					Constants.userItem=null;
					Constants.userItem=userItem;
					
					Intent intent = new Intent(SignUpCreatorPage.this, CreatorActivityOrnob.class);
					Bundle bundle = new Bundle();
					Creator1 creator = new Creator1(Constants.userItem.getUser_id(), Constants.userItem.getUser_name(), Constants.userItem.getAddress(), Constants.userItem.getEmail(), Constants.userItem.getPhn_no(), Constants.userItem.getDate_of_creation(), Constants.userItem.getLatitude(), Constants.userItem.getLongitude(), Constants.userItem.getImage_url(), Constants.userItem.getCreator_type_id());
					bundle.putParcelable("creator", creator);
					intent.putExtras(bundle);
					
					startActivity(intent);
					if(pDialog.isShowing()) pDialog.dismiss();
				}
				

			} else {
				Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
				if(pDialog.isShowing()) pDialog.dismiss();
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

	public String convertFileToString(File file) {
		FileInputStream imageInFile;
		String imageDataString = null;
		try {
			imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			// Converting Image byte array into Base64 String
			imageDataString = encodeImage(imageData);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imageDataString;
	}

	public String encodeImage(byte[] imageByteArray) {

		return new String(Base64.encodeBase64(imageByteArray));
	}

}
