package com.manoj.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;

	public class CreatePlaylistDialogFragment extends DialogFragment implements OnClickListener, android.content.DialogInterface.OnClickListener {

	    private EditText editQuantity;
	    private static PlaylistFragment playlistFragment =null;

	    public static DialogFragment newInstance(PlaylistFragment pl) {
	        DialogFragment dialogFragment = new CreatePlaylistDialogFragment();
	        playlistFragment = pl;
	        return dialogFragment;
	    }
	    
	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        editQuantity = new EditText(getActivity());
	        editQuantity.setInputType(InputType.TYPE_CLASS_TEXT);

	        return new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setMessage("Please Enter Quantity")
	                .setPositiveButton("OK", this).setNegativeButton("CANCEL", null).setView(editQuantity).create();

	    }

	    @Override
	    public void onClick(DialogInterface dialog, int position) {

	        String value = editQuantity.getText().toString();
	        Log.d("Quantity: ", value);
	        //PlaylistFragment callingActivity = (PlaylistFragment) getParentFragment();
	        playlistFragment.onUserSelectValue(value);
	        dialog.dismiss();
	    }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
}