package com.manoj.macawplayer;

import java.util.ArrayList;
import java.util.HashMap;

import com.manoj.helper.FileHandlers;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.ListActivity;

public class ThemeList extends ListActivity {
	//song list
	ArrayList songsList1=new ArrayList();
	ArrayList colorList=new ArrayList();
	@Override
	public void onCreate(Bundle savedInstanceState){
		try{
			//Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.playlist);
			ArrayList<HashMap<String, String>> colorListData = new ArrayList<HashMap<String, String>>();
			SongManager songManager=new SongManager();
			SongInfo si=new SongInfo();
			
			if(this.colorList!=null && this.colorList.isEmpty()){
			 this.colorList.add("#008EBE");
			 this.colorList.add("#9833CB");
			 this.colorList.add("#669800");
			 this.colorList.add("#FF8800");
			 this.colorList.add("#CB0000");
			}
			
				for (int i = 0; i < colorList.size(); i++) {
		            // creating new HashMap
		            
		            HashMap<String, String> tempColorListData=new HashMap<String, String>();
		            String title=colorList.get(i).toString();
		            tempColorListData.put("songTitle",title);
		            colorListData.add(tempColorListData);
		        }
	
			//adding menu items to List view
			
			ListAdapter adapter= new SimpleAdapter(this,colorListData,R.layout.playlist_item,new String[] {"songTitle"},new int[] {R.id.songTitle});
			setListAdapter(adapter);
			
			//select single listview item
			ListView lv=getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id){
					FileHandlers fileHandlers = new FileHandlers();
					fileHandlers.saveKeyValue("temp.txt", getApplicationContext(), "backgroundColor", colorList.get(position).toString());
					// Starting new intent
	                Intent in =new Intent(getApplicationContext(),MainActivity.class);
	             
	                //sending song index to player activity
	                in.putExtra("temp", 1);
	                setResult(101, in);
	             
					
					//closing playlist view
	                finish();
				}
			});
	
	}catch(Exception e){
		e.printStackTrace();
	}
	}

}
