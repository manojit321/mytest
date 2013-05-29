package com.manoj.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.manoj.bean.PlaylistBean;
import com.manoj.helper.PlaylistHandler;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;
import com.manoj.macawplayer.ThemeList;

public class PlaylistFragment extends Fragment {
	ListView cp_listview;
	private TextView frag1Title;
	private Utilities utilities;
	private LinearLayout homeScreen;
	PlaylistHandler playlistHandler;
	private ImageButton btnCreatePlaylist;
	private  PlaylistFragment playlistFragment = null;
	ListView lv;
	private static final String TAG = "dialog";
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        return (LinearLayout)inflater.inflate(R.layout.customplaylist, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        playlistFragment = this;
      // you only need to instantiate these the first time your fragment is
      // created; then, the method above will do the rest

			try{
				//Remove title bar
			//	super.onCreate(savedInstanceState);
			//	getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
				//getActivity().setContentView(R.layout.playlist);
				
				utilities = new Utilities();
				homeScreen = (LinearLayout) getActivity().findViewById(R.id.customplaylist_layout);
				utilities.colorSeter(homeScreen, getActivity().getApplicationContext());
				
				btnCreatePlaylist = (ImageButton) getActivity().findViewById(R.id.createPlaylist);
				playlistHandler = new PlaylistHandler();
	    		ArrayList<PlaylistBean> playBeans = playlistHandler.checkforplaylists(getActivity());
	    		final ArrayList<HashMap<String, String>> playlistMap = new ArrayList<HashMap<String,String>>(); 
				
					for (int i = 0; i < playBeans.size(); i++) {
			            // creating new HashMap
			            HashMap<String, String> playlist=new HashMap<String, String>();
			            String playlistName =playBeans.get(i).getPlaylistName();
			            long playlist_id = playBeans.get(i).getPlaylistId();
			            playlist.put("playlistName",playlistName);
			            playlist.put("playlist_id",playlist_id+"");
			            playlistMap.add(playlist);
			        }
		
				//adding menu items to List view
				
				ListAdapter adapter= new SimpleAdapter(getActivity(),playlistMap,R.layout.customplaylist_row,new String[] {"playlistName"},new int[] {R.id.customplaylist_name});
				lv = (ListView) getActivity().findViewById(R.id.customplaylist_listview);
				lv.setLongClickable(true);
				lv.setAdapter(adapter);
				
				//select single listview item
			/*	lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
		                    int position, long id){
					//// getting listitem index
		                int playlistIndex = position;
		                
		                String playlistName=playlistMap.get(playlistIndex).get("playlistName").toString();
			             // Starting new intent
			                Intent in =new Intent(getActivity().getApplicationContext(),MainActivity.class);
		                

			                //sending song index to player activity
			                in.putExtra("album", playlistName);
			                getActivity().setResult(200, in);
		                
		                //closing playlist view
		                getActivity().finish();
					}
				});
				
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view,
		                    int position, long id) {
						// Dialog box to get Options to delete or edit
						
						
						
						// custom dialog
						final Dialog dialog = new Dialog(getActivity().getApplicationContext());
						dialog.setContentView(R.layout.customplaylistdialog);
						dialog.setTitle("Customize playlist...");
			 
						 
						Button dialogButton = (Button) dialog.findViewById(R.id.editbutton);
						 
						dialog.show();
						
						
						
						return false;
					}
					
				});*/

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
		                    int position, long id) {
						// Dialog box to get Options to delete or edit
						
						 PlaylistDialogFragment.newInstance(playlistMap.get(position).get("playlistName"),playlistMap.get(position).get("playlist_id"),playlistFragment).show(getFragmentManager(), TAG);
					}
					
				});
				
				btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						CreatePlaylistDialogFragment.newInstance(playlistFragment).show(getFragmentManager(), TAG);
						
						/*TimerDialogFragment timerDialogFragment = (TimerDialogFragment) TimerDialogFragment.newInstance();
						timerDialogFragment.show(getFragmentManager(), "sdf");
						
						EquializerDialogFragment.newInstance(MainActivity.get(getActivity()).mp.getAudioSessionId()).show(getFragmentManager(), TAG);*/
					}
		
				});
		
		
		}catch(Exception e){
			e.printStackTrace();
		}

    }
    
    public void onUserSelectValue(String created){
    	Log.i("", "playlist created"+created);
    	if(created!=null){
    		playlistHandler.createPlaylist(getActivity().getContentResolver(), created);
    	}
    	playlistFragment.onActivityCreated(getArguments());
    }
}
