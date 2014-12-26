package com.example.zalguur;

//www.androidhive.info/2012/08/android-session-management-using-shared-preferences/
// startandroid.ru/en/lessons/complete-list/210-lesson-9-event-listeners-with-button-example.html
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	
	private Button callMnBtn;
	private Button callDeBtn;
	private EditText number;
	private String pause =",,,,";
	private String num;		// temporarily used to keep numbers
	private String numDtmf;	// numbers with pause to be sent as DTMF
	private String version = "";
	
	// PrefSettings class
    PrefSettings settings;
    
    // Alert Dialog
    PrefSettingsAlert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        number = (EditText) findViewById(R.id.number);
        callMnBtn = (Button) findViewById(R.id.callMn);
        callDeBtn = (Button) findViewById(R.id.callDe);
        
        // PrefSettings class instance
        settings = new PrefSettings(getApplicationContext());
        
        settings.checkSettings();	// check if gateway & PIN numbers are set
        
        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        try
        {
        	version = getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).versionName;
        } catch (Exception e)
        {
        	Toast.makeText(getApplicationContext(), "Version unknown!", Toast.LENGTH_LONG).show();
        }
        
        callMnBtn.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v) 
        	{
        		if (settings.isSet())	// gateway, PIN numbers are set 
        		{
					// set the data
					num = settings.getPin().toString(); // get PIN number
					numDtmf = "";
					if (num.length() > 0) // PIN number is set
					{
						// insert pause (',') between each digit in PIN number
						for (int i = 0; i < num.length(); i++) {
							numDtmf += num.charAt(i) + ",";
						}

						numDtmf += "%23,"; // '#,'

						num = number.getText().toString(); // get phone number

						if (num.length() > 0) // phone number is given
						{
							// insert pause (',') between each digit in phone number
							for (int i = 0; i < num.length(); i++) {
								numDtmf += num.charAt(i) + ",";
							}

							numDtmf += "%23"; // '#'

							try {
								// collect all
								// gateway + pause (',') + pin (DTMF ended with '#') + phone number (DTMF ended with '#'), i.e., 01511234567,,,,1,2,3,4,%23,9,9,8,8,7,6,5,4,%23
								// pause length is determined by using Asterisk, logger.conf
								String uri = "tel:" + settings.getGateway()
										+ pause + numDtmf;
								Intent callIntent = new Intent(
										Intent.ACTION_CALL, Uri.parse(uri));

								startActivity(callIntent);
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(),
										"Call failed!", Toast.LENGTH_LONG)
										.show();
								e.printStackTrace();
							}
						} else {
							alert = new PrefSettingsAlert();
							alert.showAlert(MainActivity.this,
									"Phone number is missing!",
									"Please enter phone number.", false);
						}
					} else {
						alert = new PrefSettingsAlert();
						alert.showAlert(
								MainActivity.this,
								"PIN number is missing!",
								"Please set up PIN number in SETTINGS before making any call.",
								false);
					}
				}
        		else
        		{
        			alert = new PrefSettingsAlert();
					alert.showAlert(
							MainActivity.this,
							"Gateway, PIN numbers are missing!",
							"Please set up gateway and PIN numbers in SETTINGS before making any call.",
							false);
        		}
        	}
        	
        });
        
        callDeBtn.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		num =  number.getText().toString();	// get phone number

    			if (num.length() > 0)	// phone number is given
    			{
    				try
    				{
    					String uri = "tel:" + num;
    					Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

    					startActivity(dialIntent);
    				} catch(Exception e) {
    					Toast.makeText(getApplicationContext(), "Call failed!", Toast.LENGTH_LONG).show();
    					e.printStackTrace();
    				}
    			}
    			else
    			{
    				alert = new PrefSettingsAlert();
    				alert.showAlert(MainActivity.this, "Phone number is missing!", "Please enter phone number.", false);
    			}
        	}
        });
    }

    private class MyPhoneListener extends PhoneStateListener {
    	
    	private boolean onCall = false;
    	
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {
    		
    		switch (state)
    		{
    		case TelephonyManager.CALL_STATE_RINGING:
    			// phone ringing
    			Toast.makeText(MainActivity.this, incomingNumber + " calls you", Toast.LENGTH_LONG).show();
    			break;
    			
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			// one call exists that is dialing, active, or on hold
    			Toast.makeText(MainActivity.this, "on call...", Toast.LENGTH_LONG).show();
    			onCall = true;
    			break;
    			
    		case TelephonyManager.CALL_STATE_IDLE:
    			// init of the class, phone call end
    			// detect flag from CALL_STATE_OFFHOOK
    			if (onCall == true)
    			{
    				Toast.makeText(MainActivity.this, "Call ended", Toast.LENGTH_LONG).show();
    				
    				// restart application
    				Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
    				restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				startActivity(restart);
    				
    				onCall = false;
    			}
    			break;
    			
			default:
				break;
    		}
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //menu.findItem(R.id.action_settings).setIntent(new Intent(this, PrefSettingsActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	item.setIntent(new Intent(this, PrefSettingsActivity.class));
			startActivity(item.getIntent());
            return true;
        }
        else if (id == R.id.action_about) {
        	alert = new PrefSettingsAlert();
			alert.showAlert(MainActivity.this,
					"About",
					"(c) Zalguur v." + version +
					"\nby Enkhbold \nboldoodd@yahoo.com \nfacebook.com/enkhbold.ebold.5", false);
        }
        
        return super.onOptionsItemSelected(item);
    }
}



