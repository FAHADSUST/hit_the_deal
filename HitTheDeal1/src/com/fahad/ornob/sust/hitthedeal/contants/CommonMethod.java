package com.fahad.ornob.sust.hitthedeal.contants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class CommonMethod {
	
	public CommonMethod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static  long currentTimeFrom1970(){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long millSecondsSinceEpoch = calendar.getTimeInMillis();	
		return  millSecondsSinceEpoch;
	}
	
	
	
	public void uploadImage(Context context,String file_name,File file) {
		
		
		try {
			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);

			// Converting Image byte array into Base64 String
			String imageDataString = encodeImage(imageData);

			makeVolleyRequest(context,imageDataString,file_name);
			imageInFile.close();

		} catch (FileNotFoundException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
					.show();
		} catch (IOException ioe) {
			Toast.makeText(context, ioe.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	public String encodeImage(byte[] imageByteArray) {

		return new String(Base64.encodeBase64(imageByteArray));
	}

	private void makeVolleyRequest(final Context context, final String imageString,final String file_name) {
		if (imageString == null) {
			Toast.makeText(context, "string null", Toast.LENGTH_LONG)
					.show();
		}
		StringRequest strReq = new StringRequest(Method.POST,
				Constants.urlFileUploadServlet ,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Toast.makeText(context, response.toString(),
								Toast.LENGTH_LONG).show();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(context, "fahad: "+error.toString(),
								Toast.LENGTH_LONG).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("image", imageString);
				params.put("image_name", file_name);
				return params;
			}

		};

		Volley.newRequestQueue(context).add(strReq);
	}
	
	public static File getFileForCapture() {
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
