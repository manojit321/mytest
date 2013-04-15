package com.manoj.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.manoj.helper.FileHandlers;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;
import com.manoj.macawplayer.SongManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;

@SuppressLint("NewApi")
public class ThemeList extends ListFragment {
	//song list
	ArrayList songsList1=new ArrayList();
	ArrayList colorList=new ArrayList();
	private FragmentActivity fa;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		try{
			//Remove title bar
			fa.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			fa.setContentView(R.layout.playlist);
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
			
			ListAdapter adapter= new SimpleAdapter(fa,colorListData,R.layout.playlist_item,new String[] {"songTitle"},new int[] {R.id.songTitle});
			setListAdapter(adapter);
			
			//select single listview item
			ListView lv=getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id){
					FileHandlers fileHandlers = new FileHandlers();
					fileHandlers.saveKeyValue("temp.txt", fa.getApplicationContext(), "backgroundColor", colorList.get(position).toString());
					// Starting new intent
	                Intent in =new Intent(fa.getApplicationContext(),MainActivity.class);
	             
	                //sending song index to player activity
	                in.putExtra("temp", 1);
	                fa.setResult(101, in);
	             
					
					//closing playlist view
	                fa.finish();
				}
				
			});
			return (LinearLayout)inflater.inflate(R.layout.playlist, container, false);
	
	}catch(Exception e){
		e.printStackTrace();
	}
		return container;
	}
	
}
