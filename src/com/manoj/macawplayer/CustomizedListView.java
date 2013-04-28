package com.manoj.macawplayer;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.manoj.adapters.LazyAdapter;
import com.manoj.helper.ImageLoader;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CustomizedListView extends Activity {
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	public static final String KEY_SONG = "song"; // parent node
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
    ArrayList songsList1=new ArrayList();
	private Utilities utilities;
	private LinearLayout homeScreen;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song);
		

		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		
		ArrayList<HashMap> songsListData = new ArrayList<HashMap>();
		SongManager songManager=new SongManager();
		SongInfo si=new SongInfo();
		utilities = new Utilities();
		homeScreen = (LinearLayout) findViewById(R.id.song_layout);
		//utilities.colorSeter(homeScreen,getApplicationContext());
		
		if(songsList1!=null && this.songsList1.isEmpty())
		 songsList1=si.getSongs(getContentResolver());
		
			for (int i = 0; i < songsList1.size(); i++) {
				Song songs=(Song)songsList1.get(i);
	            HashMap song1=new HashMap();
	            String title=songs.getTitle();
	            song1.put("songTitle",title);
	            song1.put(KEY_ID,"wq");
				song1.put(KEY_TITLE,songs.getTitle());
				song1.put(KEY_ARTIST,songs.getArtist());
				song1.put(KEY_DURATION,"4.10");
				song1.put(KEY_THUMB_URL,songs.getAlbumArtUrl());

	            songsListData.add(song1);
	        }


		list=(ListView)findViewById(R.id.song_list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsListData);        
        list.setAdapter(adapter);
        list.setFastScrollEnabled(true);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
							

			}
		});
	}
	

}