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
import com.manoj.macawplayer.R;

public class AvailablePlaylistDialogFragment extends DialogFragment{

	PlaylistHandler playlistHandler;
	ArrayList<PlaylistBean> playlistBeans;
	static HashMap songsListData; 
	
	public static DialogFragment newInstance(HashMap s) {
        DialogFragment dialogFragment = new AvailablePlaylistDialogFragment();
        songsListData = s;
        return dialogFragment;
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		 playlistHandler = new PlaylistHandler();
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setTitle("Playlist");
         LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View dialogLayout = inflater.inflate(R.layout.available_playlist_dialog, null);
         builder.setView(dialogLayout);
         
         playlistBeans = playlistHandler.checkforplaylists(getActivity().getApplicationContext());
         final String[] playlistNames = new String[playlistBeans.size()];
         for(int i=0;i<playlistBeans.size();i++){
        	 playlistNames[i] = playlistBeans.get(i).getPlaylistName();
         }

         builder.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, playlistNames),
                 new DialogInterface.OnClickListener() {


             public void onClick(DialogInterface dialog, int which) {
                 Log.v("touched: ", playlistNames[which].toString());
                 playlistHandler.addToPlaylist(getActivity().getContentResolver(),Integer.parseInt(songsListData.get("KEY_Unique_row_id").toString()), playlistBeans.get(which).getPlaylistId());
             }} 
             );


         return builder.create();
	}
}
