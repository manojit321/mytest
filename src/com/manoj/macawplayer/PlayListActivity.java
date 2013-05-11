package com.manoj.macawplayer;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.Toast;
import android.app.ListActivity;

public class PlayListActivity extends ListActivity {
	//song list
	ArrayList songsList1=new ArrayList();
	SongInfo sInfo=new SongInfo();
	@Override
	public void onCreate(Bundle savedInstanceState){
		try{
			//Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.playlist);
			ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
			SongManager songManager=new SongManager();
			
			
			
			
			//Get the bundle
		    Bundle bundle = getIntent().getExtras();

		    //Extract the data…
		    String playlistName = bundle.getString("playlistName"); 
		    songsList1 = sInfo.getAlbumSongs(getContentResolver(),playlistName);
			
			
			
			
			/*if(this.songsList1!=null && this.songsList1.isEmpty())
			 this.songsList1=si.getSongs(getContentResolver());*/
			
				for (int i = 0; i < songsList1.size(); i++) {
		            // creating new HashMap
		            Song songs=(Song)songsList1.get(i);
		            HashMap<String, String> song1=new HashMap<String, String>();
		            String title=songs.getTitle();
		            song1.put("songTitle",title);
		            songsListData.add(song1);
		        }
	
			//adding menu items to List view
			
			ListAdapter adapter= new SimpleAdapter(this,songsListData,R.layout.playlist_item,new String[] {"songTitle"},new int[] {R.id.songTitle});
			setListAdapter(adapter);
			
			//select single listview item
			ListView lv=getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id){
				//// getting listitem index
	                int songIndex = position;
	                
	             // Starting new intent
	                Intent in =new Intent(getApplicationContext(),MainActivity.class);
	             
	                //sending song index to player activity
	                in.putExtra("songIndex", songIndex);
	                setResult(100, in);
	                
	                //closing playlist view
	                finish();
	                startActivity(in);
				}
			});
	
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
}
