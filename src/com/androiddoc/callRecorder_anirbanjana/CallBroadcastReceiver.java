package com.androiddoc.callRecorder_anirbanjana;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

public class CallBroadcastReceiver extends BroadcastReceiver
{
	private Context mContext;
	TelephonyManager telephony;
	CustomPhoneStateListener customPhoneListener ;


	@Override
    public void onReceive(Context context, Intent intent) 
    {
		try 
		{
	        mContext = context;
	        
	        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
			{
				String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				saveInPreference("phoneNo", "OutgoingCall_" + numberToCall, context);
			}
	        
	        Bundle extras = intent.getExtras();
	        if (extras != null) {
	            String state = extras.getString(TelephonyManager.EXTRA_STATE);
	            //Log.w("DEBUG", state);
	
	                telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	                customPhoneListener = new   CustomPhoneStateListener();
	                telephony.listen(customPhoneListener,   PhoneStateListener.LISTEN_CALL_STATE);
	                Bundle bundle = intent.getExtras();
	                String phoneNr= bundle.getString("incoming_number");
	        }
		}
		catch (Exception e) 
		{
			// do something clever with the exception
			System.out.println(e.getMessage());
		}
    }

	public class CustomPhoneStateListener extends PhoneStateListener
	{
		private static final String TAG = "CustomPhoneStateListener";
		Handler handler=new Handler();
		@Override
		public void onCallStateChanged(int state, String incomingNumber) 
		{
			switch (state) 
			{
			case TelephonyManager.CALL_STATE_RINGING:
				if(!incomingNumber.equalsIgnoreCase(""))
				{
		            saveInPreference("phoneNo", "IncomingCall_" + incomingNumber, mContext);
					Log.e("State","CALL_STATE_RINGING"); 
				}
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
//				makeAToast("CALL_STATE_OFFHOOK");
				Log.e("State","CALL_STATE_OFFHOOK");
				
				if(!isMyServiceRunning("com.androiddoc.callRecorder_anirbanjana.CallRecordingService"))
	        	{
					Intent callIntent = new Intent(mContext, CallRecordingService.class);
	        		mContext.startService(callIntent);
	        	}
				
				break;

			case TelephonyManager.CALL_STATE_IDLE:
//				makeAToast("CALL_STATE_IDLE");
				Log.e("State","CALL_STATE_IDLE");
				mContext.stopService(new Intent(mContext, CallRecordingService.class));
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
			telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_NONE);
		}


	}       
	
	//------------------------------------------------------------
	//Check service is running or not
	//------------------------------------------------------------
	private boolean isMyServiceRunning(String className) 
	{
	    ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) 
	    {
	        if (className.equals(service.service.getClassName())) 
	        {
	        	Log.d("Service running",className+" is running");
	            return true;
	        }
	    }
	    Log.d("Service running",className+" not is running");
	    return false;
	}
	//--------------------------------------------------------------
	//END Check service is running or not
	//--------------------------------------------------------------


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
