package com.example.zalguur;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class PrefSettingsActivity extends Activity {
     
    // Gateway, PIN edittext
    EditText txtGateway, txtPin;
     
    // buttons
    Button btnSave, btnClear, btnCancel;
     
    // PrefSettings class
    PrefSettings settings;
    
    // Alert Dialog
    PrefSettingsAlert alert;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
         
        // PrefSettings class instance
        settings = new PrefSettings(getApplicationContext());               
         
        // Gateway, PIN input text
        txtGateway = (EditText) findViewById(R.id.txtGateway);
        txtPin = (EditText) findViewById(R.id.txtPin);
        
        // show gateway, PIN
        if (settings.isSet())
        {
        	// get settings from SharedPrefs
        	txtGateway.setText(settings.getGateway());
        	txtPin.setText(settings.getPin());
            /*HashMap<String, String> data = settings.getSettings();
            
            // show gateway, PIN numbers
            txtGateway.setText(data.get(PrefSettings.KEY_GATEWAY));
        	txtPin.setText(data.get(PrefSettings.KEY_PIN));*/
        }
         
        Toast.makeText(getApplicationContext(), "View/change settings", Toast.LENGTH_LONG).show();
         
        // buttons
        btnSave = (Button) findViewById(R.id.btnSave);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnCancel = (Button) findViewById(R.id.btnCancel);
         
        // Save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
                // Get gateway, PIN from EditText
                String gateway = txtGateway.getText().toString();
                String pin = txtPin.getText().toString();
                 
                // Check if gateway, PIN is filled               
                if((gateway.trim().length() > 4) && (pin.trim().length() > 0))
                {
                	// save settings
                	settings.saveSettings(gateway, pin);
                	
                	// restart MainActivity
                    Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    	
                    // close all Activities
                    restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        				
                    startActivity(restart);
                }
                else
                {
                	//Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();
                	alert = new PrefSettingsAlert();
                	alert.showAlert(PrefSettingsActivity.this, "Try again!", "Please enter gateway and PIN numbers correctly.", false);
                }
            }
        });
        
        // Clear button click event
        btnClear.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
            	
            	// clear SharedPrefs
            	settings.clearSettings();
            	
            	// restart MainActivity
            	Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            	
            	// close all Activities
				restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
            	startActivity(restart);
            }
        });
        
        // Cancel button click event
        btnCancel.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
            	// restart MainActivity
            	Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            	
            	// close all Activities
				restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
            	startActivity(restart);
            }
        });
        
    }       
}