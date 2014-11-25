package com.fahad.ornob.sust.hitthedeal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.contants.FileUpload;
import com.fahad.ornob.sust.hitthedeal.item.CreatorTypeItem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignupViewerPage extends Activity{
	
	private static final String TAG = SignupViewerPage.class.getSimpleName();

	Button captureImgViewrB,browImgB,signUpViwerB,fbSignUpB;
	EditText userNameVEd,emailViwerEd,passViwerEd;
	ImageView viwerImg;
	
	private static final int INITIAL_DELAY_MILLIS = 300;
	ConnectionDetector cd;
	
	private static final int CAMERA_REQUEST = 1888;
	private static final int FILE_SELECT_CODE = 0;
	
	File image;	
	Uri ImageUri;		
	public static String imageFileName=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_viwer);
		
		inti();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		captureImgViewrB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				image = FileUpload.getFileForCaptureCreatorSignUp(Constants.ViwerTypeID);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(image));
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		browImgB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
		signUpViwerB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String renameStr = null;
				
				if (imageFileName != null ) {
					renameStr = emailViwerEd.getText().toString()
							+ ".jpg";															
				}else{
					renameStr=Constants.ImgEmtyTAG;
				}
				if(showWarningDialog()){
					if(imageFileName != null ){
						FileUpload.uploadFileGetInstance(SignupViewerPage.this,imageFileName,renameStr);
					}
					String urlViwerSignUp=Constants.urlInsertSignUpData+"user_type_id="+Constants.ViwerTypeID+"&user_name="+userNameVEd.getText().toString()+"&address="+""+"&email="+emailViwerEd.getText().toString()+"&phn_no="+"0"+"&date_of_creation="+CommonMethod.currentTimeFrom1970()+"&latitude="+"0"+"&longitude="+"0"+"&image_url="+renameStr+"&password="+passViwerEd.getText().toString()+"&creator_type_id="+"1";					
					Log.e(TAG, "Response: " + urlViwerSignUp);
					jsonUniAsync(urlViwerSignUp, Constants.ViwerSignUp);															
				}
			}
		});
		fbSignUpB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void inti() {
		// TODO Auto-generated method stub
		captureImgViewrB=(Button)findViewById(R.id.captureViwerImgB);
		browImgB=(Button)findViewById(R.id.browseViwerImgB);
		signUpViwerB=(Button)findViewById(R.id.signupViwerB);
		fbSignUpB=(Button)findViewById(R.id.fbSignupViwerB);
		
		userNameVEd=(EditText)findViewById(R.id.userNameViwerSignUpEd);
		emailViwerEd=(EditText)findViewById(R.id.emailViwerSignEd);
		passViwerEd=(EditText)findViewById(R.id.pasViwerSignEd);
		
		viwerImg=(ImageView)findViewById(R.id.viwerProPicImg);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			try {  
		          Bitmap captureBmp;
		          captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(image) );
		          ImageUri = Uri.fromFile(image);
		          viwerImg.setImageBitmap(captureBmp);
		          
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
			viwerImg.setImageBitmap(bitmap);

			File destinationFile = FileUpload.getFileForCaptureCreatorSignUp(Constants.ViwerTypeID);
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
				} else if (itemType == Constants.ViwerSignUp) {
					
						Toast.makeText(this, "Suucess", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(SignupViewerPage.this,ViwerActivity.class);
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

		if (userNameVEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnOrgName, false);

		} else if (emailViwerEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnEmail, false);

		}else if (passViwerEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnPass, false);

		} else if (!validEmail(emailViwerEd.getText().toString())) {
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
