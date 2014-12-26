package com.example.zalguur;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PrefSettingsAlert {

	/**
	 * display simple alert dialog
	 */
	public void showAlert(Context context, String title, String msg, Boolean status) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		// set alert dialog title
		alertDialogBuilder.setTitle(title);
		
		// set alert dialog message
		alertDialogBuilder.setMessage(msg);
		
		// set alert dialog icon
		//if (status != null)
			//alert.setIcon((status) ? R.drawable.success : R.drawable.fail);
		
		// set OK button
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.cancel();
			}
		});
		
		// create & show alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
