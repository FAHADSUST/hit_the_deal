package com.fahad.ornob.sust.hitthedeal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.CommonMethod;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;

import com.fahad.ornob.sust.hitthedeal.item.CreatorTypeItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;

import android.app.Activity;
import android.app.ProgressDialog;
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
	String renameStr = null;
	ProgressDialog pDialog;
	Long dateOfCreation;
	
	
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
				image = CommonMethod.getFileForCapture();
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(image));
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		browImgB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
		signUpViwerB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (image != null ) {
					renameStr = emailViwerEd.getText().toString()
							+ ".jpg";															
				}else{
					renameStr=Constants.ImgEmtyTAG;
				}
				if(showWarningDialog()){
					/*if(image != null ){
						CommonMethod cm = new CommonMethod();
						cm.uploadImage(SignupViewerPage.this, renameStr, image);

					}*/
					pDialog.show();
					dateOfCreation=CommonMethod.currentTimeFrom1970();

					jsonUniAsync(Constants.urlInsertSignUpData, Constants.ViwerSignUp);															
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
		
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Progressing...");
		pDialog.setCancelable(false);
		
	}
	
	private static final int PICK_IMAGE = 1;
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			try {
				Bitmap captureBmp = Media.getBitmap(getContentResolver(),
						Uri.fromFile(image));
				Uri ImageUri = Uri.fromFile(image);
				viwerImg.setImageBitmap(captureBmp);

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
	        
	        
	        viwerImg.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));
	        
	        image = new File(imageFilePath);
	    }
	}

	private void jsonUniAsync(String url, final int itemType) {

		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			cd.showAlertDialogToNetworkConnection(this, "Connection loss", "No network connection.", false);

		} else {
			StringRequest jsonReq = new StringRequest(Method.POST, url,
					 new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response, itemType);
								pDialog.dismiss();
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
							pDialog.dismiss();
							Toast.makeText(SignupViewerPage.this, error.getMessage(), Toast.LENGTH_LONG).show();
							
						}
					}){

				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();

					params.put("user_type_id",String.valueOf(Constants.ViwerTypeID));
					params.put("user_name", userNameVEd.getText().toString());
					params.put("address", "Empty");
					params.put("email", emailViwerEd.getText().toString());
					params.put("phn_no", "0");
					params.put("date_of_creation",
							String.valueOf(dateOfCreation));
					params.put("latitude", "0");
					params.put("longitude", "0");
					params.put("image_url", renameStr);
					params.put("password", passViwerEd.getText().toString());

					params.put("creator_type_id", "1");
					
					params.put("image_name", renameStr);//convertFileToString
					if(image!=null)
						params.put("image", convertFileToString(image));
					else params.put("image", "");

					return params;
				}
			};

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
			
		}

	}

	private void parseJsonFeed(String responseString, int itemType) {
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
				} else if (itemType == Constants.ViwerSignUp) {
					
						Toast.makeText(this, "Suucess", Toast.LENGTH_SHORT)
								.show();
						int user_id = response.getInt("user_id");
						UserItem userItem = new UserItem(user_id, Constants.ViwerTypeID, userNameVEd.getText().toString(), "Empty", emailViwerEd.getText().toString(), "0", dateOfCreation, 0, 0, renameStr, passViwerEd.getText().toString(), 1);
						Constants.userItem=userItem;
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
