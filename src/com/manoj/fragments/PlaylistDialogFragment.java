package com.manoj.fragments;


import com.manoj.helper.PlaylistHandler;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.PlayListActivity;
import com.manoj.macawplayer.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

	public class PlaylistDialogFragment extends DialogFragment {

		public static String playlistName = null;
		PlaylistHandler playlistHandler;
		static PlaylistFragment playlistFragment = null;
		public static long playlist_id;
	    public static DialogFragment newInstance(String pName,String pl_id,PlaylistFragment pl) {
	        DialogFragment dialogFragment = new PlaylistDialogFragment();
	        playlistName = pName;
	        playlistFragment = pl;
	        playlist_id = Long.parseLong(pl_id);
	        return dialogFragment;
	    }

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	playlistHandler = new PlaylistHandler();
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        Log.i("", ""+playlistName);
	        //builder.setTitle(""+playlistName);
	        //builder.setView(getContentView());
	        //Dialog dialog = builder.create();
	        final Dialog dialog = new Dialog(getActivity());
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.customplaylistdialog);
	        TextView playlistTiltle = (TextView)dialog.findViewById(R.id.playlistTitle);
	        playlistTiltle.setText(playlistName);

	        Button editbutton = (Button)dialog.findViewById(R.id.editbutton);
	        editbutton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Log.i("", "view");
					Intent in =new Intent(getActivity().getApplicationContext(),PlayListActivity.class);
					//Create the bundle
				    Bundle bundle = new Bundle();
				    //Add your data from getFactualResults method to bundle
				    bundle.putString("playlistName", playlistName);  
				    bundle.putLong("playlist_id", playlist_id);
				    //Add the bundle to the intent
				    in.putExtras(bundle);
	                dialog.dismiss();  
	                //closing playlist view
	                getActivity().startActivity(in);
				}
	        });
	        
	        Button deletebutton = (Button)dialog.findViewById(R.id.deletebutton);
	        deletebutton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Log.i("", "delete");
					playlistHandler.deletePlaylist(getActivity().getContentResolver(),playlistName);
					playlistFragment.onUserSelectValue(null);
					dialog.dismiss();
				}
	        });
	        Button playbutton = (Button)dialog.findViewById(R.id.playbutton);
	        playbutton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Log.i("", "play");
					Intent in =new Intent(getActivity().getApplicationContext(),MainActivity.class);
	                //sending song index to player activity
	                in.putExtra("playlist_id", playlist_id+"");
	                getActivity().setResult(204, in);
	                dialog.dismiss();  
	                //closing playlist view
	                getActivity().finish();
				}
	        });
	        
	        return dialog;
	    }

	    private View getContentView() {
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        return inflater.inflate(R.layout.customplaylistdialog, null);
	    }
	}
