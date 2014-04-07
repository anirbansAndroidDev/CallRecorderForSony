package com.androiddoc.callRecorder_anirbanjana;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class CallRecordingService extends Service {

	MediaRecorder myRec = new MediaRecorder();
	boolean isRecMode = false;

	@Override
	public IBinder onBind(Intent arg0) 
	{
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		return START_STICKY_COMPATIBILITY;
	}

	@Override
	public void onCreate() 
	{
		//Start recording
		startRec();
		super.onCreate();
	}

	public void stopRec() 
	{
		myRec.reset();
		Toast.makeText(CallRecordingService.this, "Rec Stoped",Toast.LENGTH_SHORT).show();
		Log.i("CallRecordingService", "Rec Stoped");
		isRecMode = false;
	}

	public void startRec() 
	{
		try 
		{
//			String selectedPath = this.getApplicationContext().getFilesDir() + "/system_sound";
			String selectedPath = Environment.getExternalStorageDirectory() + "/rec_sound_by_anirban";
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss aa");
			String currentDateandTime = sdf.format(new Date());

//			Crate folder if not exist 
//			String packageName =  this.getPackageName();
			File yourDir = new File(Environment.getExternalStorageDirectory() + "/rec_sound_by_anirban");
//			File yourDir = new File(this.getApplicationContext().getFilesDir() + "/system_sound");
			if (!yourDir.exists())
			{
				yourDir.mkdirs();
			}
//			String selectedPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + packageName + "/system_sound";

			myRec.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
			myRec.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			myRec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//			myRec.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/my_rec_voice.mp3");
			myRec.setOutputFile(selectedPath + "/" + currentDateandTime + "_" + getFromPreference("phoneNo") + ".amr");
			myRec.prepare();
			myRec.start(); 

			Toast.makeText(CallRecordingService.this, "Rec Start",Toast.LENGTH_SHORT).show();
			Log.i("CallRecordingService", "Rec Start");
			Log.d("saved mp3",selectedPath + "/" + currentDateandTime + "_" + getFromPreference("phoneNo") + ".amr");
			isRecMode = true;
		} 
		catch (Exception e) 
		{
			Log.e("CallRecordingService", "Error while recording.");
		} 
	}

	@Override
	public void onDestroy() 
	{
		stopRec();
		if (isRecMode) 
		{
			//Stop recording
			myRec.stop();
		}

		myRec.release();
		super.onDestroy();
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Preference Variable
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// --------------------------------------------
	// method to save variable in preference
	// --------------------------------------------
	public void saveInPreference(String name, String content) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
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
				.getDefaultSharedPreferences(this);
		preference_return = preferences.getString(variable_name, "");

		return preference_return;
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Preference Variable
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
