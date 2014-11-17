package com.fahad.ornob.sust.hitthedeal.connectiondetector;

import com.fahad.ornob.sust.hitthedeal.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;



public class AlertDialogForAnything {

	public AlertDialogForAnything(){
		
	}
	
	public static void showAlertDialogWhenComplte(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
		
		// Setting alert dialog icon		
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	
	
}
