package com.manoj.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;
import com.manoj.macawplayer.SongManager;
import com.manoj.macawplayer.SwipeViewActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
	public class TestFragment extends Fragment {
	    /** (non-Javadoc)
	     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	     */
		private ImageButton btnequializer;
		private ImageButton btntime;
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
	        return (LinearLayout)inflater.inflate(R.layout.test, container, false);
	    }
	    
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);

	      // you only need to instantiate these the first time your fragment is
	      // created; then, the method above will do the rest

				try{
					utilities = new Utilities();
					homeScreen = (LinearLayout) getActivity().findViewById(R.id.layout_root);
					utilities.colorSeter(homeScreen, getActivity().getApplicationContext());
					btntime = (ImageButton)getActivity().findViewById(R.id.btntime);	
					btnequializer = (ImageButton)getActivity().findViewById(R.id.btnequializer);
					
					btnequializer.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							EquializerDialogFragment.newInstance(MainActivity.get(getActivity()).mp.getAudioSessionId()).show(getFragmentManager(), "TAG");
						}
			
					});
					
					btntime.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							TimerDialogFragment timerDialogFragment = (TimerDialogFragment) TimerDialogFragment.newInstance();
							timerDialogFragment.show(getFragmentManager(), "sdf");
						}
			
					});
			
			}catch(Exception e){
				e.printStackTrace();
			}

	    }
	}
	