package com.androiddoc.callRecorder_anirbanjana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallStateListener extends PhoneStateListener
{
    private Context context;
    boolean isRecMode = false;
    
    public CallStateListener(Context c) 
    {
        context = c;
    }

    public void onCallStateChanged (int state, String incomingNumber)
    {
        switch (state) 
        {
        case TelephonyManager.CALL_STATE_IDLE:
        	Log.i("CallRecorder", "CALL_STATE_IDLE, stoping recording");
        	if(isRecMode)
        	{
        		context.stopService(new Intent(context, CallRecordingService.class));
        		saveInPreference("phoneNo", "");
        		isRecMode = false;
        	}
            break;
            
        case TelephonyManager.CALL_STATE_RINGING:
            Log.d("CallRecorder", "CALL_STATE_RINGING");
            saveInPreference("phoneNo", "IncomingCall_" + incomingNumber);
            break;
            
        case TelephonyManager.CALL_STATE_OFFHOOK:
        	Log.i("CallRecorder", "CALL_STATE_OFFHOOK starting recording");
        	if(!isRecMode)
        	{
        		Intent callIntent = new Intent(context, CallRecordingService.class);
        		context.startService(callIntent);
                isRecMode = true;
        	}
            break;
        }
    }
    
  	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Preference Variable
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// --------------------------------------------
	// method to save variable in preference
	// --------------------------------------------
	public void saveInPreference(String name, String content) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(name, content);
		editor.commit();
	}

	// --------------------------------------------
	// getting content from preferences
	// --------------------------------------------
	public String getFromPreference(String variable_name) {
		String preference_return;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		preference_return = preferences.getString(variable_name, "");

		return preference_return;
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Preference Variable
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
