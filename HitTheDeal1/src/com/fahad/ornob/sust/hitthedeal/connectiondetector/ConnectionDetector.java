package com.fahad.ornob.sust.hitthedeal.connectiondetector;








import com.fahad.ornob.sust.hitthedeal.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  NetworkInfo[] info = connectivity.getAllNetworkInfo();
			  if (info != null) 
				  for (int i = 0; i < info.length; i++) 
					  if (info[i].getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }

		  }
		  return false;
	}
	
	AlertDialog.Builder alertDialog2;
	public void AlertDialogToOut(Context context){
		alertDialog2 = new AlertDialog.Builder(context);
		alertDialog2.setTitle("Exit");
		alertDialog2.setMessage("Are You Want to Exit?");
		alertDialog2.setPositiveButton("Sure",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write code here to execute after dialog
		                
		            	System.exit(1);
		            }
		        });
		alertDialog2.setNegativeButton("No",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write code here to execute after dialog
		                
		            	
		            }
		        });
		alertDialog2.show();
	}
	
	public void showAlertDialogToNetworkConnection(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	
	
	
	
}
