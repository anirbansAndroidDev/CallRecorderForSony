package com.androiddoc.main_anirbanjana;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.callrecorder_anirbanjana.R;

public class ShowRecordedCallListActivity extends ListActivity {
	
	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	static final String KEY_ID 		    = "id";
	static final String KEY_PHONE_NO    = "phone_no";
	static final String KEY_CALL_STATE  = "last_name";
	static final String KEY_DATE_TIME   = "address";
	String path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_recorded_call_list);
		
		createRecCallList();
	}
	
	public void createRecCallList() {
		Toast.makeText(this,  "No call record found.", Toast.LENGTH_LONG).show();
//		File yourDir = new File(this.getApplicationContext().getFilesDir() + "/system_sound");
		File yourDir = new File(Environment.getExternalStorageDirectory() + "/rec_sound_by_anirban");
		
		if(yourDir.isDirectory()) 
		{
			//===================================================================================
			//File Shorting
			//===================================================================================
			File[] files = yourDir.listFiles();
			Pair[] pairs = new Pair[files.length];
			for (int i = 0; i < files.length; i++)
			{
			    pairs[i] = new Pair(files[i]);
			}

			// Sort them by timestamp descending.
			Arrays.sort(pairs, Collections.reverseOrder());
			//Sort them by timestamp ascending.
			//Arrays.sort(pairs);

			// Take the sorted pairs and extract only the file part, discarding the timestamp.
			for (int i = 0; i < files.length; i++)
			{
			    files[i] = pairs[i].f;
			}
			//===================================================================================
			//File Shorting
			//===================================================================================

			menuItems.clear();
			for (File f : files) 
			{
				if (f.isFile())
				{
					String file_name 	= f.getName();
					
					String date 	    = (file_name.substring(0, 10)).replaceAll("_", "-");
					String time 	    = (file_name.substring(11, 22)).replaceAll("_", ":");
					String callState  	= file_name.substring(23, 35);
					String phoneNo    	= file_name.substring(36, (file_name.length()-4));
					
					
					Log.w("Filename", "Filename: " + file_name);
					
					Log.w("Filename", "date: " + date);
					Log.w("Filename", "time: " + time);
					Log.w("Filename", "callState: " + callState);
					Log.w("Filename", "phoneNo: " + phoneNo);
	
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put(KEY_ID, file_name);
					map.put(KEY_PHONE_NO, phoneNo + " ("+callState+")");
					map.put(KEY_CALL_STATE,callState);
					map.put(KEY_DATE_TIME, date + "    " + time);
	
					// adding HashList to ArrayList
					menuItems.add(map);
				}
			}
		}
		
		ListAdapter adapter = new SimpleAdapter(this, menuItems,R.layout.user_info_list_item,
				  new String[] { KEY_PHONE_NO, KEY_DATE_TIME, KEY_ID }, 
				  new int[] {R.id.phone_no , R.id.date_time});
		setListAdapter(adapter);

	} 
	
	//On select from the list show data
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);
		HashMap<String, String> return_data = (HashMap<String, String>) o;
		
		path = Environment.getExternalStorageDirectory() + "/rec_sound_by_anirban/" + return_data.get("id");
//		Toast.makeText(this,  path, Toast.LENGTH_LONG).show();
		
		//==================================================================================================================
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 

		alertDialog.setTitle("Play or Delete ..."); 
		alertDialog.setMessage("Would you like to play this audio?"); 
		alertDialog.setIcon(R.drawable.tick); 

		alertDialog.setNegativeButton("Play", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent();  
			    intent.setAction(android.content.Intent.ACTION_VIEW);  
			    File file = new File(path);   
			    intent.setDataAndType(Uri.fromFile(file), "audio/*");  
			    ShowRecordedCallListActivity.this.startActivity(intent); 
			} 
		}); 
		
		alertDialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) { 
				File file = new File(path);
				boolean deleted = file.delete();
				
				if(deleted)
				{
					Toast.makeText(ShowRecordedCallListActivity.this,"Thank You. It's deleted", Toast.LENGTH_LONG).show();
					dialog.cancel(); 
					createRecCallList();
				}
			} 
		}); 
		
		alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) { 
				dialog.cancel(); 
			} 
		}); 

		alertDialog.show();
		//==================================================================================================================
	}
	
	class Pair implements Comparable {
	    public long t;
	    public File f;

	    public Pair(File file) {
	        f = file;
	        t = file.lastModified();
	    }

	    public int compareTo(Object o) {
	        long u = ((Pair) o).t;
	        return t < u ? -1 : t == u ? 0 : 1;
	    }
	};
}
