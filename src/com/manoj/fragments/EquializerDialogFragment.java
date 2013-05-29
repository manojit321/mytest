package com.manoj.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.manoj.bean.PlaylistBean;
import com.manoj.helper.PlaylistHandler;
import com.manoj.helper.Song;
import com.manoj.helper.Utilities;
import com.manoj.macawplayer.R;

public class EquializerDialogFragment extends DialogFragment{

	Utilities utilities;
	static HashMap songsListData; 
	ArrayList presets; 
	static int audioSession;
	int priority = 10;
	public static DialogFragment newInstance(int s) {
        DialogFragment dialogFragment = new EquializerDialogFragment();
        audioSession = s;
        return dialogFragment;
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		 utilities = new Utilities();
		 presets = new ArrayList();
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setTitle("Equializer");
         LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View dialogLayout = inflater.inflate(R.layout.available_playlist_dialog, null);
         builder.setView(dialogLayout);
         
         presets = utilities.availableEqualizer(priority, audioSession);
         final String[] presetNames = new String[presets.size()];
         for(int i=0;i<presets.size();i++){
        	 presetNames[i] = presets.get(i).toString();
         }

         builder.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, presetNames),
                 new DialogInterface.OnClickListener() {


             public void onClick(DialogInterface dialog, int which) {
                 Log.v("touched: ", presetNames[which].toString());
                 utilities.setEquializer(priority, audioSession,Short.parseShort(which+""));
             }} 
             );


         return builder.create();
	}
}
