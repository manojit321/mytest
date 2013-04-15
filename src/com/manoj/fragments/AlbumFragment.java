package com.manoj.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;
import com.manoj.macawplayer.SongManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
	 
	/**
	 * @author mwho
	 *
	 */
	public class AlbumFragment extends Fragment {
	    /** (non-Javadoc)
	     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	     */
		private TextView frag1Title;
		ArrayList songsList1=new ArrayList();
		private Utilities utilities;
		private LinearLayout homeScreen;		
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
	        return (LinearLayout)inflater.inflate(R.layout.album, container, false);
	    }
	    
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);

	      // you only need to instantiate these the first time your fragment is
	      // created; then, the method above will do the rest

				try{
					//Remove title bar
				//	super.onCreate(savedInstanceState);
				//	getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
					//getActivity().setContentView(R.layout.playlist);
					final ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
					SongManager songManager=new SongManager();
					SongInfo si=new SongInfo();
					utilities = new Utilities();
					homeScreen = (LinearLayout) getActivity().findViewById(R.id.album_layout);
					utilities.colorSeter(homeScreen, getActivity().getApplicationContext());
					if(songsList1!=null && this.songsList1.isEmpty())
					 songsList1=si.getAlbums(getActivity().getContentResolver());
					
						for (int i = 0; i < songsList1.size(); i++) {
				            // creating new HashMap
				            HashMap<String, String> song1=new HashMap<String, String>();
				            String title=songsList1.get(i).toString();
				            song1.put("songTitle",title);
				            songsListData.add(song1);
				        }
			
					//adding menu items to List view
					
					ListAdapter adapter= new SimpleAdapter(getActivity(),songsListData,R.layout.album_item,new String[] {"songTitle"},new int[] {R.id.songTitle});
					ListView lv = (ListView) getActivity().findViewById(R.id.album_list);
					lv.setAdapter(adapter);
					
					//select single listview item
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
			                    int position, long id){
						//// getting listitem index
			                int songIndex = position;
			                
			             // Starting new intent
			                Intent in =new Intent(getActivity().getApplicationContext(),MainActivity.class);
			             
			                //sending song index to player activity
			                in.putExtra("album", songsListData.get(songIndex).get("songTitle").toString());
			                getActivity().setResult(200, in);
			                
			                //closing playlist view
			                getActivity().finish();
						}
					});
			
			}catch(Exception e){
				e.printStackTrace();
			}

	    }
	}