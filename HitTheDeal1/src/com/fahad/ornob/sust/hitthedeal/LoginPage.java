package com.fahad.ornob.sust.hitthedeal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.BootUpReceiver;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.AlertDialogForAnything;
import com.fahad.ornob.sust.hitthedeal.connectiondetector.ConnectionDetector;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.contants.DataBaseKeys;
import com.fahad.ornob.sust.hitthedeal.item.CreatorTypeItem;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import databaseEntities.Creator1;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
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

@SuppressLint("NewApi")
public class LoginPage extends Activity {

	EditText emailEd, passEd;
	Button loginB, fbLoginB, signUpCreatorB, signUpViwerB;
	private YoYo.YoYoString rope, rope2, rope4;
	private View mTarget, mTarget2, mTarget3, mTarget4;
	TextView signUpTxt, forgetPassTxt;

	private static final String TAG = LoginPage.class.getSimpleName();
	private static final int INITIAL_DELAY_MILLIS = 300;
	ConnectionDetector cd;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);
		getActionBar().hide();
		
		///////////////////////////////////notificationer////////////////
		 Intent intent = new Intent(getApplicationContext(),
		 BootUpReceiver.class);
		 sendBroadcast(getIntent());
		 
		 ///////////////////////////notificationer////////////////
		Constants.Distance= getValueSharedPref(Constants.Keydist);
		
		initLogin();
		listenerLogin();
		new animationLoginAsync().execute();

	}

	class animationLoginAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mTarget.setVisibility(View.INVISIBLE);
			mTarget2.setVisibility(View.INVISIBLE);
		}

		protected String doInBackground(String... args) {
			// Building Parameters

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			mTarget.setVisibility(View.VISIBLE);
			mTarget2.setVisibility(View.VISIBLE);
			rope = YoYo.with(Techniques.ZoomInUp).duration(2000)
					.playOn(mTarget);
			rope2 = YoYo.with(Techniques.SlideInUp).duration(3000)
					.playOn(mTarget2);
			rope4 = YoYo.with(Techniques.Swing).duration(2500).playOn(mTarget4);

		}
	}

	private void listenerLogin() {
		// TODO Auto-generated method stub
		loginB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (showWarningDialog()) {
					
					
					String url = Constants.urlLogin + "email="
							+ emailEd.getText().toString() + "&password="
							+ passEd.getText().toString();
					jsonUniAsync(url, Constants.loginType);
				}
			}
		});
		fbLoginB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * YoYo.with(Techniques.SlideInUp) .duration(700)
				 * .playOn(findViewById(R.id.fbLoginRA));
				 */
			}
		});
		signUpTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mTarget3.getVisibility() == View.VISIBLE) {					
					mTarget3.setVisibility(View.INVISIBLE);
				}else{
					mTarget3.setVisibility(View.VISIBLE);
					YoYo.with(Techniques.SlideInDown).duration(600)
							.playOn(mTarget3);
				}
			}
		});
		signUpCreatorB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				gotoNextPage(Constants.CreatorSignUpPage);

			}
		});
		signUpViwerB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				gotoNextPage(Constants.ViwerSignUpPage);
			}
		});

		mTarget4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YoYo.with(Techniques.Swing).duration(700).playOn(mTarget4);
			}
		});

	}

	private void initLogin() {
		// TODO Auto-generated method stub
		emailEd = (EditText) findViewById(R.id.emailLoginED);
		passEd = (EditText) findViewById(R.id.passwordLoginED);

		loginB = (Button) findViewById(R.id.loginB);
		signUpCreatorB = (Button) findViewById(R.id.creatorB);
		signUpViwerB = (Button) findViewById(R.id.viwerB);
		fbLoginB = (Button) findViewById(R.id.fbLoginB);

		signUpTxt = (TextView) findViewById(R.id.signUpTxt);
		forgetPassTxt = (TextView) findViewById(R.id.forgotPassTxt);

		mTarget = this.findViewById(R.id.loginRL);
		mTarget2 = this.findViewById(R.id.fbLoginRA);
		mTarget3 = this.findViewById(R.id.signUpRL);
		mTarget4 = this.findViewById(R.id.logoAnimImg);

		mTarget3.setVisibility(View.INVISIBLE);
		
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Progressing...");
		pDialog.setCancelable(false);
		
		printKeyHash(this);

	}

	private void jsonUniAsync(String url, final int itemType) {

		cd = new ConnectionDetector(this);
		if (cd.isConnectingToInternet()) {
			pDialog.show();
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
							if(pDialog.isShowing()) pDialog.dismiss();
						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);

		} else {
			
			
			Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
		}

	}

	private void parseJsonFeed(JSONObject response, int itemType) {
		try {

			int success = response.getInt(DataBaseKeys.Success);
			if (success == 1) {
				if (itemType == Constants.loginType) {
					
					JSONObject jsonObject = response
							.getJSONObject(DataBaseKeys.UserDataTAG);
					UserItem userItem = new UserItem(
							jsonObject.getInt(DataBaseKeys.userKey[0]),
							jsonObject.getInt(DataBaseKeys.userKey[1]),
							jsonObject.getString(DataBaseKeys.userKey[2]),
							jsonObject.getString(DataBaseKeys.userKey[3]),
							jsonObject.getString(DataBaseKeys.userKey[4]),
							jsonObject.getString(DataBaseKeys.userKey[5]),
							jsonObject.getLong(DataBaseKeys.userKey[6]),
							jsonObject.getDouble(DataBaseKeys.userKey[7]),
							jsonObject.getDouble(DataBaseKeys.userKey[8]),
							jsonObject.getString(DataBaseKeys.userKey[9]),
							jsonObject.getString(DataBaseKeys.userKey[10]),
							jsonObject.getInt(DataBaseKeys.userKey[11]));
					
					int userTypeID = userItem.getUser_type_id();
					Constants.userItem=null;
					Constants.userItem = userItem;

					
					if (userTypeID == Constants.Creator) {
						Toast.makeText(this, "Creator", Toast.LENGTH_SHORT)
								.show();
						gotoNextPage(Constants.CreatorTypeID);
					} else {
						Toast.makeText(this, "Visitor", Toast.LENGTH_SHORT)
								.show();
						gotoNextPage(Constants.ViwerTypeID);
					}
				}
			} else {
				Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
				if(pDialog.isShowing()) pDialog.dismiss();
			}

		} catch (JSONException e) {

			if(pDialog.isShowing()) pDialog.dismiss();
			e.printStackTrace();
			Toast.makeText(this, "Fail: "+e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void gotoNextPage(int typeid) {
		// TODO Auto-generated method stub
		if (typeid == Constants.CreatorActivityPage) {
			
			
			Intent intent = new Intent(LoginPage.this, CreatorActivityOrnob.class);
			Bundle bundle = new Bundle();
			Creator1 creator = new Creator1(Constants.userItem.getUser_id(), Constants.userItem.getUser_name(), Constants.userItem.getAddress(), Constants.userItem.getEmail(), Constants.userItem.getPhn_no(), Constants.userItem.getDate_of_creation(), Constants.userItem.getLatitude(), Constants.userItem.getLongitude(), Constants.userItem.getImage_url(), Constants.userItem.getCreator_type_id());
			bundle.putParcelable("creator", creator);
			intent.putExtras(bundle);
			
			startActivity(intent);
			if(pDialog.isShowing()) pDialog.dismiss();
		} else if (typeid == Constants.ViwerActivityPage) {
			
			Intent intent = new Intent(LoginPage.this, ViwerActivity.class);
			startActivity(intent);
			if(pDialog.isShowing()) pDialog.dismiss();
		} else if (typeid == Constants.ViwerSignUpPage) {
			Intent intent = new Intent(LoginPage.this, SignupViewerPage.class);
			startActivity(intent);

		} else if (typeid == Constants.CreatorSignUpPage) {
			Intent intent = new Intent(LoginPage.this, SignUpCreatorPage.class);
			startActivity(intent);
		}

		//finish();
	}

	public boolean showWarningDialog() {

		/*
		 * if(.getText().toString().isEmpty()){
		 * AlertDialogForAnything.showAlertDialogWhenComplte(this,
		 * Constants.warnTitle, Constants.warnUserName, false);
		 * 
		 * }else
		 */
		if (emailEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnEmail, false);

		} else if (passEd.getText().toString().isEmpty()) {
			AlertDialogForAnything.showAlertDialogWhenComplte(this,
					Constants.warnTitle, Constants.warnPass, false);

		} else if (!validEmail(emailEd.getText().toString())) {
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
	
	
	public  float getValueSharedPref(String key){
		SharedPreferences prefs = getSharedPreferences("my_fref", MODE_PRIVATE); 
		Float restoredText = prefs.getFloat(key, 2);
		
		return restoredText;
	}
	
	 public static String printKeyHash(Activity context) {
			PackageInfo packageInfo;
			String key = null;
			try {

				//getting application package name, as defined in manifest
				String packageName = context.getApplicationContext().getPackageName();

				//Retriving package info
				packageInfo = context.getPackageManager().getPackageInfo(packageName,
						PackageManager.GET_SIGNATURES);
				
				Log.e("Package Name=", context.getApplicationContext().getPackageName());
				
				for (Signature signature : packageInfo.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					key = new String(Base64.encode(md.digest(), 0));
				
					// String key = new String(Base64.encodeBytes(md.digest()));
					Log.e("Key Hash=", key);

				}
			} catch (NameNotFoundException e1) {
				Log.e("Name not found", e1.toString());
			}

			catch (NoSuchAlgorithmException e) {
				Log.e("No such an algorithm", e.toString());
			} catch (Exception e) {
				Log.e("Exception", e.toString());
			}

			return key;
		}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		if (mTarget3.getVisibility() == View.VISIBLE) {					
			mTarget3.setVisibility(View.INVISIBLE);
		}
		
		emailEd.setText("");
		passEd.setText("");
		
		super.onResume();
		
	}
	 
	 
}
