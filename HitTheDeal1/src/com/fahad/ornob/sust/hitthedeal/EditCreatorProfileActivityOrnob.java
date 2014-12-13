package com.fahad.ornob.sust.hitthedeal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.customImageView.CustomNetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import databaseEntities.Creator1;

@SuppressLint("NewApi")
public class EditCreatorProfileActivityOrnob extends Activity {

	boolean isDown = true;
	Animation  slideIn, slideOut;
	Button saveButton, cancelButton;
	FrameLayout  mapLayout;
	EditText nameEditText, addressEditText, phnEditText;
	Spinner typeSpinner;
	ArrayAdapter<String> spinnerAdapter;
	CustomNetworkImageView profileImageView;
	ImageView grabLocationButton;
	String[] creatorTypes = { "Local Business", "Restaurant", "Cause",
			"Cultural", "Other" };
	Creator1 creator;
	GoogleMap googleMap;
	LatLng currentLatLng=null;
	ProgressBar progressBar;
	AlertDialog confirmationDialog;
	Button exitMapButton;
	
	
	ImageButton captureImgView, browsImgView;
	File image=null;
	String renameStr=null;
	private static final int CAMERA_REQUEST = 1888;
	private static final int PICK_IMAGE = 1;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_edit_creator);
		getActionBar().hide();

		if(imageLoader==null){
			imageLoader = AppController.getInstance().getImageLoader();
		}
		
		creator = CreatorActivityOrnob.creator;

		currentLatLng = new LatLng(creator.getLatitude(),
				creator.getLongitude());
		

		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(
				creator.getLatitude(), creator.getLongitude())), 15.0f));
		updateMapView();

		googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {
				dialogInIt(latLng);
			}
		});

		profileImageView = (CustomNetworkImageView) findViewById(R.id.profie_image_view4);
		profileImageView.setDefaultImageResId(R.drawable.default_profic);

		spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, creatorTypes);
		typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(spinnerAdapter);

		nameEditText = (EditText) findViewById(R.id.nameEt);
		addressEditText = (EditText) findViewById(R.id.addressEt);
		phnEditText = (EditText) findViewById(R.id.phnEt);
		

		slideIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_in);
		slideOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_out);

		mapLayout = (FrameLayout) findViewById(R.id.map_layout6);

		saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validityChecking()) {
					progressBar.setVisibility(View.VISIBLE);
					
					if(image!=null){
						renameStr = CreatorActivityOrnob.creator.getEmail() +Constants.PROFIC+CommonMethod.currentTimeFrom1970()+ ".jpg";			
						//CommonMethod cm = new CommonMethod();
						//cm.uploadImage(EditCreatorProfileActivityOrnob.this, renameStr, image);
					}else{
						renameStr = CreatorActivityOrnob.creator.getImageUrl();		
					}
					
					makeVolleyRequest();
				}
			}
		});

		cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		grabLocationButton = (ImageView) findViewById(R.id.grab_location_button);
		grabLocationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				slideMap();
			}
		});

		exitMapButton = (Button) findViewById(R.id.exit_map_button);
		exitMapButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				slideMap();
			}
		});

		captureImgView = (ImageButton) findViewById(R.id.take_snap_button);
		browsImgView = (ImageButton) findViewById(R.id.browse_snap_button);
		
		captureImgView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				image = CommonMethod.getFileForCapture();
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(image));
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
				
			}
		});
		browsImgView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
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
			}
		});
		
		updateViews();
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
		if (nameEditText.getText().toString().equals("")) {
			Methods.makeToast(EditCreatorProfileActivityOrnob.this,
					"Name Field Cannot Be Empty", Toast.LENGTH_LONG);
			return false;
		} else if (addressEditText.getText().toString().equals("")) {
			Methods.makeToast(EditCreatorProfileActivityOrnob.this,
					"Address Field Cannot Be Empty", Toast.LENGTH_LONG);
			return false;
		}  else if (phnEditText.getText().toString().equals("")) {
			Methods.makeToast(EditCreatorProfileActivityOrnob.this,
					"Phone no. Date Cannot Be After End Date",
					Toast.LENGTH_LONG);
			return false;
		}

		return true;
	}

	public boolean isEmailValid(final String hex) {
		Pattern pattern;
		Matcher matcher;

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		return matcher.matches();

	}

	

	void slideMap() {
		if (mapLayout.getVisibility() == View.INVISIBLE) {
			mapLayout.setVisibility(View.VISIBLE);
			mapLayout.startAnimation(slideIn);
			grabLocationButton.setVisibility(View.INVISIBLE);
			// googleMap.animateCamera(CameraUpdateFactory
			// .newLatLngZoom((new LatLng(currentLatLng.latitude,
			// currentLatLng.longitude)), 15.0f));

		} else {
			mapLayout.startAnimation(slideOut);
			mapLayout.setVisibility(View.INVISIBLE);
			grabLocationButton.setVisibility(View.VISIBLE);
		}
	}

	void updateMapView() {
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(currentLatLng.latitude, currentLatLng.longitude))
				.title(creator.getUserName()));
	}

	void updateViews() {
		nameEditText.setText(creator.getUserName());
		addressEditText.setText(creator.getAddress());
		phnEditText.setText(creator.getPhnNo());
		
		typeSpinner.setSelection(Arrays.asList(creatorTypes).indexOf(
				creator.getCreatorType()));
		profileImageView.setImageUrl(Constants.urlgetImgServlet+CreatorActivityOrnob.creator.getImageUrl(), imageLoader);
	}

	void dialogInIt(final LatLng latLng) {
		confirmationDialog = new AlertDialog.Builder(
				EditCreatorProfileActivityOrnob.this).create();

		confirmationDialog.setButton("Set",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						currentLatLng = latLng;
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

	public void makeVolleyRequest() {

		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "UpdateCreator",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray jsonArray = new JSONArray(response);
								JSONObject successObject = jsonArray
										.getJSONObject(0);
								if (successObject.getBoolean("success") == true) {
									Methods.makeToast(
											EditCreatorProfileActivityOrnob.this,
											"Profile Succesfully Updated",
											Toast.LENGTH_LONG);
									progressBar.setVisibility(View.INVISIBLE);
									creator.setUserName(nameEditText.getText()
											.toString());
									creator.setAddress(addressEditText
											.getText().toString());
									creator.setEmail(CreatorActivityOrnob.creator.getEmail());
									creator.setPhnNo(phnEditText.getText()
											.toString());
									creator.setCreatorTypeId(typeSpinner
											.getSelectedItemPosition() + 1);
									creator.setLatitude(currentLatLng.latitude);
									creator.setLongitude(currentLatLng.longitude);
									
									creator.setImageUrl(renameStr);
									
									finish();
								} else {
									progressBar.setVisibility(View.INVISIBLE);
								}

							} catch (JSONException e) {
								progressBar.setVisibility(View.INVISIBLE);
								Methods.makeToast(
										EditCreatorProfileActivityOrnob.this,
										"Update Not Successful",
										Toast.LENGTH_LONG);
							}
						} else {

						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						progressBar.setVisibility(View.INVISIBLE);
						Methods.makeToast(EditCreatorProfileActivityOrnob.this,
								"Network Error", Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", Integer.toString(creator.getUserId()));
				params.put("user_name", nameEditText.getText().toString());
				params.put("address", addressEditText.getText().toString());
				params.put("email", CreatorActivityOrnob.creator.getEmail());
				params.put("phn_no", phnEditText.getText().toString());
				params.put("latitude", Double.toString(currentLatLng.latitude));
				params.put("longitude",
						Double.toString(currentLatLng.longitude));
				params.put("creator_type_id", Integer.toString(typeSpinner
						.getSelectedItemPosition() + 1));
				
				params.put("image_url", renameStr);
				params.put("image_name", renameStr);// convertFileToString
				if (image != null)
					params.put("image", CommonMethod.convertFileToString(image));
				else
					params.put("image", "");


				//image_url

				return params;
			}

		};

		Volley.newRequestQueue(EditCreatorProfileActivityOrnob.this).add(strReq);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mapLayout.getVisibility() == View.VISIBLE) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
