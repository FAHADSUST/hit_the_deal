package com.fahad.ornob.sust.hitthedeal;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.FacebookRequestError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import com.menor.easyfacebookconnect.EasyLoginListener;
import com.menor.easyfacebookconnect.ui.EasyFacebookFragmentActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

public class SignupViewerPage extends EasyFacebookFragmentActivity {

	private static final String TAG = SignupViewerPage.class.getSimpleName();

	ImageButton captureImgViewrB, browImgB;
	Button  signUpViwerB, fbSignUpB;
	EditText userNameVEd, emailViwerEd, passViwerEd;
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

	String fbUserName, fbEmail;
	boolean isFB = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_viwer);

		checkStatus();
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
				isFB=false;
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
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_IMAGE);
				} catch (android.content.ActivityNotFoundException ex) {
					// Potentially direct the user to the Market with a Dialog
					Toast.makeText(getApplicationContext(),
							"Please install a File Manager.",
							Toast.LENGTH_SHORT).show();
				}
				
				isFB=false;
			}
		});
		signUpViwerB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isFB=false;
				if (image != null) {
					renameStr = emailViwerEd.getText().toString()  +Constants.PROFIC+CommonMethod.currentTimeFrom1970()+ ".jpg";
				} else {
					renameStr = "";
				}
				if (showWarningDialog()) {
					
					
					dateOfCreation = CommonMethod.currentTimeFrom1970();

					jsonUniAsync(Constants.urlInsertSignUpData,
							Constants.ViwerSignUp);
				}
			}
		});
		fbSignUpB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//pDialog.show();
				isFB = true;
				connect();
			}
		});
	}

	private void inti() {
		// TODO Auto-generated method stub
		captureImgViewrB = (ImageButton) findViewById(R.id.captureViwerImgB);
		browImgB = (ImageButton) findViewById(R.id.browseViwerImgB);
		signUpViwerB = (Button) findViewById(R.id.signupViwerB);
		fbSignUpB = (Button) findViewById(R.id.fbSignupViwerB);
		fbSignUpB.setVisibility(View.INVISIBLE);

		userNameVEd = (EditText) findViewById(R.id.userNameViwerSignUpEd);
		emailViwerEd = (EditText) findViewById(R.id.emailViwerSignEd);
		passViwerEd = (EditText) findViewById(R.id.pasViwerSignEd);

		viwerImg = (ImageView) findViewById(R.id.viwerProPicImg);

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

			viwerImg.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));

			image = new File(imageFilePath);
		}
	}

	private void jsonUniAsync(String url, final int itemType) {

		cd = new ConnectionDetector(this);
		if (!cd.isConnectingToInternet()) {
			
			cd.showAlertDialogToNetworkConnection(this, "Connection loss",
					"No network connection.", false);

		} else {
			pDialog.show();
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
							Toast.makeText(SignupViewerPage.this,
									error.getMessage(), Toast.LENGTH_LONG)
									.show();

						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();

					params.put("user_type_id",
							String.valueOf(Constants.ViwerTypeID));
					if (isFB)
						params.put("user_name", fbUserName);
					else
						params.put("user_name", userNameVEd.getText()
								.toString());
					params.put("address", "Empty");
					if (isFB)
						params.put("email", fbEmail);
					else
						params.put("email", emailViwerEd.getText().toString());
					params.put("phn_no", "0");
					params.put("date_of_creation",
							String.valueOf(dateOfCreation));
					params.put("latitude", "0");
					params.put("longitude", "0");
					params.put("image_url", renameStr);
					if (isFB)
						params.put("password", "");
					else
						params.put("password", passViwerEd.getText().toString());

					params.put("creator_type_id", "1");

					params.put("image_name", renameStr);// convertFileToString
					if (image != null)
						params.put("image", CommonMethod.convertFileToString(image));
					else
						params.put("image", "");

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
//						Toast.makeText(this, "Creator", Toast.LENGTH_SHORT)
//								.show();
					} else {
//						Toast.makeText(this, "Visitor", Toast.LENGTH_SHORT)
//								.show();
					}
				} else if (itemType == Constants.ViwerSignUp) {
					
						Toast.makeText(this, "Suucess", Toast.LENGTH_SHORT)
								.show();
						int user_id = response.getInt("user_id");
						
						UserItem userItem=null;
						if(isFB){
							userItem = new UserItem(user_id, Constants.ViwerTypeID, fbUserName, "Empty", fbEmail, "0", dateOfCreation, 0, 0, renameStr, "", 1);
						}else{
							userItem = new UserItem(user_id, Constants.ViwerTypeID, userNameVEd.getText().toString(), "Empty", emailViwerEd.getText().toString(), "0", dateOfCreation, 0, 0, renameStr, passViwerEd.getText().toString(), 1);
							
						}
						Constants.userItem=null;
						Constants.userItem=userItem;
						Intent intent = new Intent(SignupViewerPage.this,ViwerActivity.class);
						startActivity(intent);
						isFB=false;
						finish();
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

		if (userNameVEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnOrgName, false);

		} else if (emailViwerEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnEmail, false);

		} else if (passViwerEd.getText().toString().isEmpty()) {
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

	
	////////////////////////new////////////////////////////

	@Override
    protected void onResume() {
        super.onResume();
        checkStatus();
    }
	
	private void checkStatus() {
        if (isConnected()) {
            updateLoginUi();
        } else {
            udateLogoutUi();
        }
    }

    private void updateLoginUi() {
    }

    private void udateLogoutUi() {
        
    }

	private void connect() {
		if (isConnected()) {
			disconnect();
			udateLogoutUi();
		} // else {
		connect(new EasyLoginListener() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(com.facebook.Response response) {
				super.onSuccess(response);
				Toast.makeText(SignupViewerPage.this, "Successfully Login",
				Toast.LENGTH_LONG).show();
				/*String downloadUrl = "http://graph.facebook.com/"
						+ getUserName() + "/picture";
				fbUserName = getUserName();
				fbEmail = getUserEmail();
				userNameVEd.setText(fbUserName);
				if(pDialog.isShowing()) pDialog.dismiss();*/
				//new ImageDownloader(downloadUrl, getUserEmail());
			}

			@Override
			public void onError(FacebookRequestError error) {
				super.onError(error);
				if(pDialog.isShowing()) pDialog.dismiss();
				isFB=false;
				 Toast.makeText(SignupViewerPage.this,
				 error.getErrorMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onClosedLoginFailed(Session session,
					SessionState state, Exception exception) {
				super.onClosedLoginFailed(session, state, exception);
				Toast.makeText(SignupViewerPage.this, state.toString(),
						Toast.LENGTH_LONG).show();
				// progressDialog.dismiss();
			}

			@Override
			public void onCreated(Session session, SessionState state,
					Exception exception) {
				super.onCreated(session, state, exception);
				 Toast.makeText(SignupViewerPage.this, state.toString(),
				 Toast.LENGTH_LONG).show();
			}

			@Override
			public void onClosed(Session session, SessionState state,
					Exception exception) {
				super.onClosed(session, state, exception);
				// progressDialog.dismiss();
				 Toast.makeText(SignupViewerPage.this, state.toString(),
				 Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFinish() {
				super.onFinish();

				updateLoginUi();

			}

		});
		// }
	}

	Bitmap bmp;

	private class ImageDownloader extends AsyncTask {

		String downloadUrl;
		String imageName;

		public ImageDownloader(String downloadUrl, String imageName) {
			// TODO Auto-generated constructor stub
			this.downloadUrl = downloadUrl;
			this.imageName = imageName;
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");

		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			// TODO Auto-generated method stub
			bmp = downloadBitmap(downloadUrl);
			return bmp;
		}

		@Override
		protected void onPostExecute(Object result2) {
			// TODO Auto-generated method stub
			Log.i("Async-Example", "onPostExecute Called");
			if (bmp != null) {
				saveImageToSD();
			}
			if (image != null) {
				renameStr = fbEmail +Constants.PROFIC+CommonMethod.currentTimeFrom1970()+ ".jpg";
			} else {
				renameStr = "";
			}
			if (showWarningDialog()) {				
				dateOfCreation = CommonMethod.currentTimeFrom1970();

				jsonUniAsync(Constants.urlInsertSignUpData,
						Constants.ViwerSignUp);
			}
		}

		private Bitmap downloadBitmap(String url) {
			// initilize the default HTTP client object
			final DefaultHttpClient client = new DefaultHttpClient();
			// forming a HttoGet request
			final HttpGet getRequest = new HttpGet(url);
			try {
				HttpResponse response = client.execute(getRequest);
				// check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;
				}
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						// getting contents from the stream
						inputStream = entity.getContent();
						// decoding stream data back into image Bitmap that
						// android understands
						final Bitmap bitmap = BitmapFactory
								.decodeStream(inputStream);
						return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for
				// IOException
				getRequest.abort();
				Log.e("ImageDownloader", "Something went wrong while"
						+ " retrieving bitmap from " + url + e.toString());
			}

			return null;
		}

		FileOutputStream fos;

		private void saveImageToSD() {
			/*--- this method will save your downloaded image to SD card ---*/
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			/*--- you can select your preferred CompressFormat and quality. 
			 * I'm going to use JPEG and 100% quality ---*/
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			/*--- create a new file on SD card ---*/

			File file = CommonMethod.getFileForCapture();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*--- create a new FileOutputStream and write bytes to file ---*/
			try {
				fos = new FileOutputStream(file);
				image = file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				fos.write(bytes.toByteArray());
				fos.close();
				// Toast.makeText(this, "Image saved",
				// Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
