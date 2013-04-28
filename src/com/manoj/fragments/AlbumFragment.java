package com.manoj.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;
import com.manoj.macawplayer.SongManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
	 
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
		// Listview Adapter
	    private ArrayAdapter<String> adapter;
	    // Search EditText
	    EditText inputSearch;
	    private ListView lv;
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
					List<String> products = new ArrayList<String>();;
					if(songsList1!=null && this.songsList1.isEmpty())
					 songsList1=si.getAlbums(getActivity().getContentResolver());
					
						for (int i = 0; i < songsList1.size(); i++) {
				            // creating new HashMap
				            HashMap<String, String> song1=new HashMap<String, String>();
				            String title=songsList1.get(i).toString();
				            song1.put("songTitle",title);
				            songsListData.add(song1);
				            products.add(title);
				        }
					//adding menu items to List view
					inputSearch = (EditText) getActivity().findViewById(R.id.album_search);
					// Adding items to listview
			        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.album_item, R.id.songTitle, products);
					//ListAdapter adapter= new SimpleAdapter(getActivity(),songsListData,R.layout.album_item,new String[] {"songTitle"},new int[] {R.id.songTitle});
					 lv = (ListView) getActivity().findViewById(R.id.album_list);
					 lv.setCacheColorHint(utilities.cacheColorHint(lv, getActivity().getApplicationContext()));
					 lv.setOnCreateContextMenuListener(this);
					 
					lv.setCacheColorHint(utilities.cacheColorHint(lv, getActivity().getApplicationContext()));
					lv.setAdapter(adapter);
					
					
					

					
					
					
					//select single listview item
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
			                    int position, long id){
						//// getting listitem index
			                int songIndex = position;
			                
			               String albumName=(String) parent.getItemAtPosition(position);
			             // Starting new intent
			                Intent in =new Intent(getActivity().getApplicationContext(),MainActivity.class);
			             
			                //sending song index to player activity
			                in.putExtra("album", albumName);
			                getActivity().setResult(200, in);
			                
			                //closing playlist view
			                getActivity().finish();
						}
					});
					
					inputSearch.addTextChangedListener(new TextWatcher() {
						 
					    @Override
					    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					        // When user changed the Text
					        AlbumFragment.this.adapter.getFilter().filter(cs);
					    }
					 
					    @Override
					    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					            int arg3) {
					        // TODO Auto-generated method stub
					    }
					 
						@Override
						public void afterTextChanged(Editable arg0) {
							// TODO Auto-generated method stub
						}
					});
			
			}catch(Exception e){
				e.printStackTrace();
			}

	    }
	}