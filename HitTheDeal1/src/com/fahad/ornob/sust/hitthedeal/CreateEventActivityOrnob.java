package com.fahad.ornob.sust.hitthedeal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import necessaryMethods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.customImageView.CustomNetworkImageView;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import customviews.ObservableScrollView;
import customviews.ScrollViewListener;

@SuppressLint("NewApi")
public class CreateEventActivityOrnob extends Activity implements
		View.OnClickListener, ScrollViewListener {
	ObservableScrollView scrollView;
	LinearLayout slidingLayout;
	CustomNetworkImageView  profileImageView;
	Button createButton, openBrowserButton, pickStartDateButton,
			pickEndDateButton, openMapButton, exitMap;
	EditText nameET, descriptionET, linkET, startDateET, endDateET, latitudeET,
			longitudeET;
	LatLng latLng=null;
	GoogleMap googleMap;
	AlertDialog confirmationDialog;
	FrameLayout mapLayout;
	Animation slideIn, slideOut, slideUp, slideDown;
	ProgressBar progressBar;
	String profileImageUrl = "";//http://api.androidhive.info/feed/img/cosmos.jpg
	Calendar startDate, endDate;
	AlertDialog alertDialog;
	AlertDialog fromDatePickerDialog, toDatePickerDialog;
	
	ImageButton captureImgView, browsImgView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		// startDate = Calendar.getInstance();
		// endDate = Calendar.getInstance();

		latLng = new LatLng(CreatorActivityOrnob.creator.getLatitude(),
				CreatorActivityOrnob.creator.getLongitude());
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(
				latLng.latitude, latLng.longitude)), 15.0f));
		updateMapView();

		googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {
				dialogInIt(latLng);
			}
		});

		scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
		scrollView.setScrollViewListener(this);

		slideIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_in);
		slideOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_out);
		slideUp = AnimationUtils.loadAnimation(CreateEventActivityOrnob.this,
				R.anim.slide_up);
		slideDown = AnimationUtils.loadAnimation(CreateEventActivityOrnob.this,
				R.anim.slide_down);

		slidingLayout = (LinearLayout) findViewById(R.id.sliding_layout);
		slidingLayout.setAlpha(0.90f);

		mapLayout = (FrameLayout) findViewById(R.id.map_layout);
		profileImageView = (CustomNetworkImageView) findViewById(R.id.profie_image_view);
		profileImageView.setDefaultImageResId(R.drawable.event_default);

		nameET = (EditText) findViewById(R.id.eventNameET);
		descriptionET = (EditText) findViewById(R.id.descriptionET);
		linkET = (EditText) findViewById(R.id.linkET);
		startDateET = (EditText) findViewById(R.id.startDateET);
		endDateET = (EditText) findViewById(R.id.endDateET);
		latitudeET = (EditText) findViewById(R.id.latitudeET);
		longitudeET = (EditText) findViewById(R.id.longitudeET);

		createButton = (Button) findViewById(R.id.createButton);
		openBrowserButton = (Button) findViewById(R.id.openBrowser);
		pickStartDateButton = (Button) findViewById(R.id.startDatePicker);
		pickEndDateButton = (Button) findViewById(R.id.endDatePicker);
		openMapButton = (Button) findViewById(R.id.openMapButton);
		exitMap = (Button) findViewById(R.id.exit_map_button);
		
		captureImgView = (ImageButton) findViewById(R.id.take_snap_button);
		browsImgView = (ImageButton) findViewById(R.id.browse_snap_button);
		

		createButton.setOnClickListener(this);
		openBrowserButton.setOnClickListener(this);
		pickStartDateButton.setOnClickListener(this);
		pickEndDateButton.setOnClickListener(this);
		openMapButton.setOnClickListener(this);
		exitMap.setOnClickListener(this);
		
		captureImgView.setOnClickListener(this);
		browsImgView.setOnClickListener(this);

		pickEndDateButton.setClickable(false);
	}

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		if (oldy > y) {
			slidingLayout.setVisibility(View.VISIBLE);
			slidingLayout.startAnimation(slideUp);
		} else if (oldy < y) {
			slidingLayout.startAnimation(slideDown);
			slidingLayout.setVisibility(View.INVISIBLE);
		}
	}

	File image=null;
	private static final int CAMERA_REQUEST = 1888;
	private static final int PICK_IMAGE = 1;
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.createButton:
			if (validityChecking()) {
				progressBar.setVisibility(View.VISIBLE);
				
				
				if(image!=null){
					String renameStr = CreatorActivityOrnob.creator.getEmail()+CommonMethod.currentTimeFrom1970()+".jpg";
					profileImageUrl=renameStr;			
					CommonMethod cm = new CommonMethod();
					cm.uploadImage(CreateEventActivityOrnob.this, renameStr, image);
				}else{
					profileImageUrl="Empty";
				}

				makeVolleyRequest();
				
			}
			break;
		case R.id.openBrowser:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("about:blank")));
			break;
		case R.id.startDatePicker:
			createFromDatePicker();
			break;
		case R.id.endDatePicker:
			createToDatePicker();
			break;
		case R.id.openMapButton:
			latitudeET
					.setText("Latitude : " + Double.toString(latLng.latitude));
			longitudeET.setText("Longitude : "
					+ Double.toString(latLng.longitude));
			slideMap();
			break;

		case R.id.exit_map_button:
			slideMap();
			break;
			
		case R.id.take_snap_button:
			Intent cameraIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			image = CommonMethod.getFileForCapture();
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(image));
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
			
			break;
			
		case R.id.browse_snap_button:

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			try {
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
			} catch (android.content.ActivityNotFoundException ex) {
				// Potentially direct the user to the Market with a Dialog
				Toast.makeText(getApplicationContext(),
						"Please install a File Manager.",
						Toast.LENGTH_SHORT).show();
			}
			
			break;

		default:
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			try {
				Bitmap captureBmp = Media.getBitmap(getContentResolver(),
						Uri.fromFile(image));
				Uri ImageUri = Uri.fromFile(image);
				
				
				profileImageView.setLocalImageBitmap(captureBmp);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("Fahad", e.toString());
			}

		} else if(resultCode == RESULT_OK && requestCode == PICK_IMAGE && intent != null && intent.getData() != null) {
	        Uri _uri = intent.getData();
	        Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();

	        final String imageFilePath = cursor.getString(0);
	        cursor.close();
	        
	        
	        profileImageView.setLocalImageBitmap(BitmapFactory.decodeFile(imageFilePath));
	        
	        image = new File(imageFilePath);
	    }
	}

	boolean validityChecking() {
		if (nameET.getText().toString().equals("")) {
			Methods.makeToast(CreateEventActivityOrnob.this,
					"Title Field Cannot Be Empty", Toast.LENGTH_LONG);
			return false;
		} else if (descriptionET.getText().toString().equals("")) {
			Methods.makeToast(CreateEventActivityOrnob.this,
					"Description Field Cannot Be Empty", Toast.LENGTH_LONG);
			return false;
		} else if (startDateET.getText().toString().equals("")
				|| endDateET.getText().toString().equals("")) {
			Methods.makeToast(CreateEventActivityOrnob.this,
					"Date Fields Cannot Be Empty", Toast.LENGTH_LONG);
			return false;
		} else if (startDate.compareTo(endDate) > 0) {
			Methods.makeToast(CreateEventActivityOrnob.this,
					"Start Date Cannot Be After End Date", Toast.LENGTH_LONG);
			return false;
		} else if (latitudeET.getText().toString().equals("")
				|| longitudeET.getText().toString().equals("")) {
			Methods.makeToast(CreateEventActivityOrnob.this,
					"Locations Fields Cannot Be Empty", Toast.LENGTH_LONG);
			return false;
		}else if(image==null){
			Methods.makeToast(CreateEventActivityOrnob.this,
					"You have to give your event image.", Toast.LENGTH_LONG);
			return false;
		}
		return true;
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

	void slideMap() {
		if (mapLayout.getVisibility() == View.INVISIBLE) {
			mapLayout.setVisibility(View.VISIBLE);
			mapLayout.startAnimation(slideIn);
			openMapButton.setVisibility(View.INVISIBLE);

		} else {
			mapLayout.startAnimation(slideOut);
			mapLayout.setVisibility(View.INVISIBLE);
			openMapButton.setVisibility(View.VISIBLE);
		}
	}

	void updateMapView() {
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(latLng.latitude, latLng.longitude)).title(
				CreatorActivityOrnob.creator.getUserName()));
	}

	public void makeVolleyRequest() {
		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "CreateEvent",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray jsonArray = new JSONArray(response);
								JSONObject successObject = jsonArray
										.getJSONObject(0);
								if (successObject.getBoolean("success") == true) {
									Methods.makeToast(CreateEventActivityOrnob.this,
											"Event Succesfully Created",
											Toast.LENGTH_LONG);
									progressBar.setVisibility(View.INVISIBLE);
									finish();
								} else {
									progressBar.setVisibility(View.INVISIBLE);
								}

							} catch (JSONException e) {
								progressBar.setVisibility(View.INVISIBLE);
								Methods.makeToast(CreateEventActivityOrnob.this,
										"Update Not Successful",
										Toast.LENGTH_LONG);
							}
						} else {

						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						NetworkResponse networkResponse = error.networkResponse;
						if (networkResponse == null) {
							Methods.makeToast(CreateEventActivityOrnob.this,
									"network null", Toast.LENGTH_LONG);
						}
						progressBar.setVisibility(View.INVISIBLE);
						Methods.makeToast(CreateEventActivityOrnob.this,
								error.toString(), Toast.LENGTH_LONG);
						// Methods.makeToast(CreateEventActivity.this,
						// "Network Error", Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("creator_id",
						Integer.toString(CreatorActivityOrnob.creator.getUserId()));
				params.put("event_name", nameET.getText().toString());
				params.put("event_description", descriptionET.getText()
						.toString());
				params.put("start_date",
						Long.toString(startDate.getTimeInMillis()));
				params.put("end_date", Long.toString(endDate.getTimeInMillis()));
				params.put("event_img", profileImageUrl);
				params.put("event_url", linkET.getText().toString());
				params.put("latitude", Double.toString(latLng.latitude));
				params.put("longitude", Double.toString(latLng.longitude));

				return params;
			}
		};

		Volley.newRequestQueue(CreateEventActivityOrnob.this).add(strReq);
	}

	@SuppressWarnings("deprecation")
	void dialogInIt(final LatLng latLng) {
		confirmationDialog = new AlertDialog.Builder(CreateEventActivityOrnob.this)
				.create();

		confirmationDialog.setButton("Set",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CreateEventActivityOrnob.this.latLng = latLng;
						latitudeET.setText("Latitude : "
								+ Double.toString(latLng.latitude));
						longitudeET.setText("Longitude : "
								+ Double.toString(latLng.longitude));
						updateMapView();
						confirmationDialog.dismiss();
					}
				});

		confirmationDialog.setButton2("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						confirmationDialog.dismiss();
					}
				});

		String message = "Latitude : " + latLng.latitude + "\nLongitude : "
				+ latLng.longitude;
		confirmationDialog.setMessage(message);
		confirmationDialog.setIcon(R.drawable.marker_white);
		confirmationDialog.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mapLayout.getVisibility() == View.VISIBLE) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void createFromDatePicker() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CreateEventActivityOrnob.this);
		// Get the layout inflater
		LayoutInflater inflater = CreateEventActivityOrnob.this.getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.date_picker, null);
		final DatePicker datePicker = (DatePicker) dialogView
				.findViewById(R.id.date);
		final TimePicker timePicker = (TimePicker) dialogView
				.findViewById(R.id.time);

		TextView datePickerTextView = (TextView) dialogView
				.findViewById(R.id.date_picker_text);
		datePickerTextView.setText("Start Date");
		timePicker.setIs24HourView(true);

		startDate = Calendar.getInstance();
		datePickerTextView.setText("Start Date");
		datePicker.updateDate(startDate.get(Calendar.YEAR),
				startDate.get(Calendar.MONTH),
				startDate.get(Calendar.DAY_OF_MONTH));
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(startDate.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(startDate.get(Calendar.MINUTE));
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(dialogView)
				// Add action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						pickEndDateButton.setClickable(true);
						fromDatePickerDialog.dismiss();
						int day = datePicker.getDayOfMonth();
						int month = datePicker.getMonth();
						int year = datePicker.getYear();

						int hours = timePicker.getCurrentHour();
						int minutes = timePicker.getCurrentMinute();

						startDate.set(year, month, day, hours, minutes, 0);
						setTimeTextView("Start Date", startDateET, startDate);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								fromDatePickerDialog.dismiss();
							}
						});

		fromDatePickerDialog = builder.create();
		fromDatePickerDialog.setCancelable(false);
		fromDatePickerDialog.show();
	}

	public void createToDatePicker() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CreateEventActivityOrnob.this);
		// Get the layout inflater
		LayoutInflater inflater = CreateEventActivityOrnob.this.getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.date_picker, null);
		final DatePicker datePicker = (DatePicker) dialogView
				.findViewById(R.id.date);
		final TimePicker timePicker = (TimePicker) dialogView
				.findViewById(R.id.time);

		TextView datePickerTextView = (TextView) dialogView
				.findViewById(R.id.date_picker_text);
		datePickerTextView.setText("End Date");
		timePicker.setIs24HourView(true);

		// endDate = Calendar.getInstance();
		datePicker.updateDate(startDate.get(Calendar.YEAR),
				startDate.get(Calendar.MONTH),
				startDate.get(Calendar.DAY_OF_MONTH));
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(startDate.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(startDate.get(Calendar.MINUTE));
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(dialogView)
				// Add action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						toDatePickerDialog.dismiss();

						int day = datePicker.getDayOfMonth();
						int month = datePicker.getMonth();
						int year = datePicker.getYear();

						int hours = timePicker.getCurrentHour();
						int minutes = timePicker.getCurrentMinute();

						Calendar calendar = Calendar.getInstance();
						calendar.set(year, month, day, hours, minutes, 0);

						if (calendar.compareTo(startDate) < 0) {
							Toast.makeText(getApplicationContext(),
									"End Time Can Not Before Start Time",
									Toast.LENGTH_LONG).show();
							createToDatePicker();
						} else {
							endDate = calendar;
							setTimeTextView("End Date", endDateET, endDate);
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								toDatePickerDialog.dismiss();

							}
						});

		toDatePickerDialog = builder.create();
		toDatePickerDialog.setCancelable(false);
		toDatePickerDialog.show();
	}

	void setTimeTextView(String string, TextView tV, Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault());
		int year = calendar.get(Calendar.YEAR);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		String message = string + " : " + day + "-" + month + "-" + year + "  "
				+ hours + ":" + minutes;
		tV.setText(message);
	}
}
