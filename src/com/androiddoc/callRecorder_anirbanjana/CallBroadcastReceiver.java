package com.androiddoc.callRecorder_anirbanjana;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

public class CallBroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent) 
    {
        
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
        {
            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            saveInPreference("phoneNo", "OutgoingCall_" + numberToCall, context);
        }

        CallStateListener phoneListener = new CallStateListener(context);
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Preference Variable
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// --------------------------------------------
	// method to save variable in preference
	// --------------------------------------------
	public void saveInPreference(String name, String content, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(name, content);
		editor.commit();
	}

	// --------------------------------------------
	// getting content from preferences
	// --------------------------------------------
	public String getFromPreference(String variable_name, Context context) {
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
