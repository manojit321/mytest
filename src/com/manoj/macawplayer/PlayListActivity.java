package com.manoj.macawplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.manoj.helper.PlaylistHandler;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.SwipeDetector;

public class PlayListActivity extends ListActivity {
	//song list
	ArrayList songsList1=new ArrayList();
	SongInfo sInfo=new SongInfo();
	PlaylistHandler playlistHandler;
	String playlistName = null;
	long playlist_id;
	Bundle savedInsState = null;
	SimpleAdapter adapter;
	ArrayList<HashMap<String, String>> songsListData;
	
	
	// Messages for Handler
	public static final int MSG_UPDATE_ADAPTER 		= 0;
	public static final int MSG_CHANGE_ITEM 		= 1;
	public static final int MSG_ANIMATION_REMOVE 	= 2;
	
	// Messages for the context menu
	public static final int MSG_REMOVE_ITEM 		= 10;
	public static final int MSG_RENAME_ITEM 		= 11;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what)
			{
			case MSG_UPDATE_ADAPTER: // ListView updating
				adapter.notifyDataSetChanged();
				break;
			case MSG_ANIMATION_REMOVE: // Start animation removing
				View view = (View)msg.obj;
				view.startAnimation(getDeleteAnimation(0, (msg.arg2 == 0) ? -view.getWidth() : 2 * view.getWidth(), msg.arg1));
				break;
			}
		}
	};
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		try{
			//Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.playlist);
			songsListData = new ArrayList<HashMap<String, String>>();
			SongManager songManager=new SongManager();
			playlistHandler = new PlaylistHandler();
			savedInsState = savedInstanceState;
			
			
			//Get the bundle
		    Bundle bundle = getIntent().getExtras();

		    //Extract the data…
		    playlistName = bundle.getString("playlistName"); 
		    playlist_id = bundle.getLong("playlist_id");
		    //songsList1 = sInfo.getAlbumSongs(getContentResolver(),playlistName);
		    songsList1 = playlistHandler.songsforplaylists(getApplicationContext(), playlist_id+"");
			
			
			
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
			
			adapter= new SimpleAdapter(this,songsListData,R.layout.playlist_item,new String[] {"songTitle"},new int[] {R.id.songTitle});
			
			//select single listview item
			ListView lv=getListView();
			lv.setAdapter(adapter);
/*			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id){
				//// getting listitem index
	                int songIndex = position;
	                
	             // Starting new intent
	                Intent in =new Intent(getApplicationContext(),MainActivity.class);
	                Song song = (Song)songsList1.get(songIndex);
	             
	                //sending song index to player activity
	                in.putExtra("album", song.getAlbum());
	                in.putExtra("title_key", song.getTitle_key());
	                setResult(300, in);
	                
	                //closing playlist view
	                finish();
	                //startActivity(in);
	                MainActivity mainActivity = MainActivity.get(getApplicationContext());
	                mainActivity.onActivityResult(204, 204, in);
				}
			});
*/			
			
			registerForContextMenu(lv); // Register the context menu for ListView (called long press on an item)
			// Swipe detection
			final SwipeDetector swipeDetector = new SwipeDetector();
			lv.setOnTouchListener(swipeDetector);
					
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {		
					// If there is a tap on the Header or Footer, do not do anything
					/*if (position == 0 || position == songsList1.size() + 1)
						return;*/
					Message msg = new Message();
					Log.i("", "position      "+position);
					msg.arg1 = position;// If was detected swipe we delete an item
					if (swipeDetector.swipeDetected()){
						Log.i("", "swipeDetected      ");
	                    if (swipeDetector.getAction() == SwipeDetector.Action.LR || 
	                    		swipeDetector.getAction() == SwipeDetector.Action.RL)
	                    {
	                    	Log.i("", "swipeDetected      LR or RL");
	                    	msg.what = MSG_ANIMATION_REMOVE;
	                    	msg.arg2 = swipeDetector.getAction() == SwipeDetector.Action.LR ? 1 : 0;
	                    	msg.obj = view;
	                    	handler.sendMessage(msg);
	                    }
	                } 
					// Otherwise, select an item
					else {
					//// getting listitem index
		                int songIndex = position;
		                
		             // Starting new intent
		                Intent in =new Intent(getApplicationContext(),MainActivity.class);
		                Song song = (Song)songsList1.get(songIndex);
		             
		                //sending song index to player activity
		                in.putExtra("playlist_id", playlist_id+"");
		                in.putExtra("title_key", song.getTitle_key());
		                setResult(300, in);
		                
		                //closing playlist view
		                finish();
		                //startActivity(in);
		                MainActivity mainActivity = MainActivity.get(getApplicationContext());
		                mainActivity.onActivityResult(204, 204, in);
					}
				}
			});

	
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	
	
	/**
	 * Starting animation remove
	 */
	private Animation getDeleteAnimation(float fromX, float toX, int position)
	{
		Animation animation = new TranslateAnimation(fromX, toX, 0, 0);
		animation.setStartOffset(100);
		animation.setDuration(800);
		animation.setAnimationListener(new DeleteAnimationListenter(position));
		animation.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.anticipate_overshoot_interpolator));
		return animation;
	}

	
	/**
	 * Listenter used to remove an item after the animation has finished remove
	 */
	public class DeleteAnimationListenter implements Animation.AnimationListener
	{
		 private int position;
		 public DeleteAnimationListenter(int position)
		 {
			 this.position = position;
		 }
		 @Override
		 public void onAnimationEnd(Animation arg0) {		
			 removeItem(position);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

				
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}	
	}
	/**
	 * Removing an item from the list
	 */
	private void removeItem(int index) {
		playlistHandler.removeFromPlaylist(getContentResolver(), ((Song)songsList1.get(index)).getAudio_id(),playlist_id);
		Log.i("", "removeItem     "+index+" "+playlist_id);
		songsListData.remove(index);
		songsList1.remove(index);
		handler.sendEmptyMessage(PlayListActivity.MSG_UPDATE_ADAPTER);
	}

}
