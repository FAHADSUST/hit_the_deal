package com.fahad.ornob.sust.hitthedeal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EditViewerProfileActivity extends Activity {
	private static final String TAG = EditViewerProfileActivity.class
			.getSimpleName();

	Button editViwerProB;
	FeedImageView editViwerProImgView;
	Button editViwerProCaptureImgView, editViwerProBrowsImgView;
	EditText editViwerEmailEd, editViwerPhoneEd, editViwerAddressEd,
			editViwerPassEd;
	EditText editViwerOrgNameEd;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_profile);

		init();
		setValue();
		listener();

	}

	private void init() {
		// TODO Auto-generated method stub

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		editViwerProB = (Button) findViewById(R.id.editViwerProB);
		editViwerProImgView = (FeedImageView) findViewById(R.id.editViwerProImgView);
		editViwerProCaptureImgView = (Button) findViewById(R.id.editViwerProCaptureImgView);
		editViwerProBrowsImgView = (Button) findViewById(R.id.editViwerProBrowsImgView);
		editViwerEmailEd = (EditText) findViewById(R.id.editViwerEmailEd);
		editViwerPhoneEd = (EditText) findViewById(R.id.editViwerPhoneEd);
		editViwerAddressEd = (EditText) findViewById(R.id.editViwerAddressEd);
		editViwerPassEd = (EditText) findViewById(R.id.editViwerPassEd);
		editViwerOrgNameEd = (EditText) findViewById(R.id.editViwerOrgNameEd);

	}

	private void setValue() {
		// TODO Auto-generated method stub
		editViwerOrgNameEd.setText(Constants.userItem.getUser_name());
		editViwerEmailEd.setText(Constants.userItem.getEmail());
		if (!Constants.userItem.getPhn_no().isEmpty())
			editViwerPhoneEd.setText(Constants.userItem.getPhn_no());
		else
			editViwerPhoneEd.setHint("Please add phone number");
		if (!Constants.userItem.getAddress().isEmpty())
			editViwerAddressEd.setText(Constants.userItem.getAddress());
		else
			editViwerAddressEd.setHint("Please add your address");
		editViwerPassEd.setText(Constants.userItem.getPassword());

		if (Constants.userItem.getImage_url() != null) {
			editViwerProImgView.setImageUrl(Constants.urlgetImgServlet+Constants.userItem.getImage_url(),
					imageLoader);
			editViwerProImgView.setVisibility(View.VISIBLE);
			editViwerProImgView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		}

	}

	// editViwerPhoneEd//editViwerAddressEd//
	public boolean showWarningDialog() {

		if (editViwerOrgNameEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnOrgName, false);

		} else if (editViwerEmailEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnEmail, false);

		} else if (editViwerAddressEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnAddess, false);

		} else if (editViwerPassEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnPass, false);

		} else if (!validEmail(editViwerEmailEd.getText().toString())) {
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

	private void jsonUniAsync(String url, final int itemType) {

		ConnectionDetector cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			cd.showAlertDialogToNetworkConnection(this, "Connection loss",
					"No network connection.", false);

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

			} else {
				Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	File image=null;

	public static String imageFileName = null;

	private void listener() {
		// TODO Auto-generated method stub
		editViwerProCaptureImgView
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent cameraIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						image = getFileForCapture();
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(image));
						startActivityForResult(cameraIntent, CAMERA_REQUEST);
					}
				});
		editViwerProBrowsImgView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
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

		editViwerProB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String renameStr = editViwerEmailEd.getText().toString()+".jpg";
				CommonMethod cm = new CommonMethod();
				cm.uploadImage(EditViewerProfileActivity.this, renameStr, image);


			}
		});

	}

	private static final int CAMERA_REQUEST = 1888;
	private static final int PICK_IMAGE = 1;

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			try {
				Bitmap captureBmp = Media.getBitmap(getContentResolver(),
						Uri.fromFile(image));
				Uri ImageUri = Uri.fromFile(image);
				editViwerProImgView.setImageBitmap(captureBmp);

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
	        
	        
	        editViwerProImgView.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));
	        
	        image = new File(imageFilePath);
	    }
	}

	public File getFileForCapture() {
		// TODO Auto-generated method stub
		final File path = Constants.mainFolderpath;
		if (!path.exists()) {
			path.mkdir();
		}
		int n = 100000;
		int rand = new Random().nextInt(n);
		String name = "Image-" + rand + ".jpg";
		File fileimage = new File(path, name);

		return fileimage;
	}
}
