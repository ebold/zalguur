package com.example.zalguur;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class PrefSettings {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "ZalguurSettings";
     
    // are SharedPrefs set
    private static final String IS_SET = "IsSet";
    
    // gateway number (make variable public to access from outside)
    public static final String KEY_GATEWAY = "gateway";
     
    // PIN number (make variable public to access from outside)
    public static final String KEY_PIN = "pin";
     
    // Constructor
    public PrefSettings(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Save settings
     * */
    public void saveSettings(String gateway, String pin){

        // Sharedprefs are set
    	editor.putBoolean(IS_SET, true);
    	
        // store gateway in Sharedpref
        editor.putString(KEY_GATEWAY, gateway);
         
        // store PIN in Sharedpref
        editor.putString(KEY_PIN, pin);
        
        // commit changes
        editor.commit();
    }  
     
    /**
     * Get stored gateway number
     * */
    public String getGateway(){
        // gateway number
   		return pref.getString(KEY_GATEWAY, null).toString();
    }
    
    /**
     * Get stored PIN number
     * */
    public String getPin(){
        // pin number
    	return pref.getString(KEY_PIN, null).toString();
    }
    
    /**
     * Get stored data
     */
    public HashMap<String, String> getSettings() {
    	HashMap<String, String> data = new HashMap<String, String>();
    	
    	// get gateway number
    	data.put(KEY_GATEWAY, pref.getString(KEY_GATEWAY, null));
    	
    	// get PIN number
    	data.put(KEY_PIN, pref.getString(KEY_PIN, null));
    	
    	return data;
    }
    
    /**
     * Check if Sharedprefs are set
     */
    public void checkSettings() {
    	if(!this.isSet()) {
    		// SharedPrefs are not set, redirect to PrefSettingsActitivy
    		Intent intent = new Intent(_context, PrefSettingsActivity.class);
    		
    		// close all Activities
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		
    		// add new Flag to start new Activity
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		
    		// start PrefSettingsActivity
    		_context.startActivity(intent);
    	}
    }
    
    /**
     * Check for settings
     */
    public boolean isSet() {
    	return pref.getBoolean(IS_SET, false);
    }
    
    /**
     * Clear settings
     */
    public void clearSettings() {
    	// clear all data in SharedPrefs
    	editor.clear();
    	editor.commit();
    	
    	/* Code section below is not used, because clear button click event in PrefSettingsActivity restarts MainActivity
    	// SharedPrefs are not set, redirect to PrefSettingsActitivy
		Intent intent = new Intent(_context, PrefSettingsActivity.class);
		
		// close all Activities
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// add new Flag to start new Activity
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// start PrefSettingsActivity
		_context.startActivity(intent);*/
    }
}