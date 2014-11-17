package com.fahad.ornob.sust.hitthedeal;


import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class LoginPage extends Activity {
	
	EditText emailEd,passEd;
	Button loginB,fbLoginB,signUpCreatorB,signUpViwerB;
	private YoYo.YoYoString rope,rope2,rope4;
	private View mTarget,mTarget2,mTarget3,mTarget4;
	TextView signUpTxt,forgetPassTxt;
	
	private static final String TAG = LoginPage.class.getSimpleName();
	private static final int INITIAL_DELAY_MILLIS = 300;
	ConnectionDetector cd;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_page);
        getActionBar().hide();
        initLogin();
        listenerLogin();
        showOrHideLogin(Constants.Show);        
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
			rope = YoYo.with(Techniques.ZoomInUp).duration(2000).playOn(mTarget);
	        rope2= YoYo.with(Techniques.SlideInUp).duration(3000).playOn(mTarget2);
	        rope4  = YoYo.with(Techniques.Swing).duration(2500).playOn(mTarget4);
 
        }
    }

    private void listenerLogin() {
		// TODO Auto-generated method stub
		loginB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(showWarningDialog()){
					String url=Constants.urlLogin+"email="+emailEd.getText().toString()+"&password="+passEd.getText().toString();    	
					loginAsync(url);
				}
			}
		});
		fbLoginB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*YoYo.with(Techniques.SlideInUp)
		        .duration(700)
		        .playOn(findViewById(R.id.fbLoginRA));*/
			}
		});
		signUpTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*mTarget3.setVisibility(View.VISIBLE);
				YoYo.with(Techniques.SlideInDown)
		        .duration(700)
		        .playOn(mTarget3);*/
			}
		});
		signUpCreatorB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YoYo.with(Techniques.SlideOutUp)
		        .duration(700)
		        .playOn(mTarget3);
			}
		});
		signUpViwerB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YoYo.with(Techniques.SlideOutUp)
		        .duration(700)
		        .playOn(mTarget3);
			}
		});
		
		mTarget4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YoYo.with(Techniques.Swing)
		        .duration(700)
		        .playOn(mTarget4);
			}
		});
		
	}


	private void initLogin() {
		// TODO Auto-generated method stub
		emailEd = (EditText)findViewById(R.id.emailLoginED);
		passEd = (EditText)findViewById(R.id.passwordLoginED);
		
		loginB = (Button)findViewById(R.id.loginB);
		signUpCreatorB = (Button)findViewById(R.id.creatorB);
		signUpViwerB=(Button)findViewById(R.id.viwerB);		
		fbLoginB = (Button)findViewById(R.id.fbLoginB);
		
		signUpTxt = (TextView)findViewById(R.id.signUpTxt);
		forgetPassTxt = (TextView)findViewById(R.id.forgotPassTxt);
		
		mTarget = this.findViewById(R.id.loginRL);
        mTarget2 = this.findViewById(R.id.fbLoginRA);
        mTarget3 = this.findViewById(R.id.signUpRL);
        mTarget4 = this.findViewById(R.id.logoAnimImg);
        
        mTarget3.setVisibility(View.INVISIBLE);
        
	}

	public void showOrHideLogin(int showOrHide){
		if(showOrHide==Constants.Show){
			findViewById(R.id.login_page_include_id).setVisibility(View.VISIBLE);
			findViewById(R.id.signup_creator_page_include_id).setVisibility(View.GONE);
		}else if(showOrHide==Constants.Hide){
			findViewById(R.id.login_page_include_id).setVisibility(View.GONE);
			findViewById(R.id.signup_creator_page_include_id).setVisibility(View.VISIBLE);
		}
	}
	

	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	private void loginAsync(String url) {
		
    	cd = new ConnectionDetector(this);
    	if(!cd.isConnectingToInternet()){
			Cache cache = AppController.getInstance().getRequestQueue().getCache();
			Entry entry = cache.get(url);
			if (entry != null) {
				// fetch the data from cache
				try {
					String data = new String(entry.data, "UTF-8");
					try {
						parseJsonFeed(new JSONObject(data));
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
								parseJsonFeed(response);
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
	
	private void parseJsonFeed(JSONObject response) {
		try {
			
			 int success = response.getInt(DataBaseKeys.Success);
			 if(success==1){
				 int userTypeID = response.getInt(DataBaseKeys.user_type_id);
				 if(userTypeID==Constants.Creator){
					 Toast.makeText(this, "Creator", Toast.LENGTH_SHORT).show();
				 }else{
					 Toast.makeText(this, "Visitor", Toast.LENGTH_SHORT).show();
				 }
			 }else{
				 Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
			 }
			
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
	
	
	public boolean showWarningDialog(){
		
		/*if(.getText().toString().isEmpty()){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnUserName, false);
    		
		}else */
		if(emailEd.getText().toString().isEmpty()){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnEmail, false);
    		
		}else if(passEd.getText().toString().isEmpty()){
			AlertDialogForAnything.showAlertDialogWhenComplte(this, Constants.warnTitle, Constants.warnPass, false);
    		
		}else if(!validEmail(emailEd.getText().toString())){
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
