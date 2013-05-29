package com.manoj.fragments;

import com.manoj.helper.Utilities;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.PlayListActivity;
import com.manoj.macawplayer.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class TimerDialogFragment extends DialogFragment implements OnCompletionListener,SeekBar.OnSeekBarChangeListener{

	TextView timer_text;
	int time;
	private SeekBar timerBar;
	private Handler myhandle = new Handler();
	 public static DialogFragment newInstance() {
	        DialogFragment dialogFragment = new TimerDialogFragment();
	        return dialogFragment;
	    }


	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 final Dialog dialog = new Dialog(getActivity());
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.timer_dialog);
	        timer_text = (TextView)dialog.findViewById(R.id.timer_text);
	        timerBar = (SeekBar) dialog.findViewById(R.id.timerBar);
	        
	     // Listeners
	        timerBar.setOnSeekBarChangeListener(this);
	        
	        Button okbutton = (Button)dialog.findViewById(R.id.okbutton);
	        okbutton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Log.i("", "view");
					myhandle.postDelayed(mUpdateTimeTask,Utilities.minuteToMilliseconds(time));
					dialog.dismiss();
				}
	        });
	        
	        Button cancelbutton = (Button)dialog.findViewById(R.id.cancelbutton);
	        cancelbutton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Log.i("", "cancelbutton");
					dialog.dismiss();
				}
	        });
	        
	        
	        ImageButton addTime = (ImageButton)dialog.findViewById(R.id.addTime);
	        addTime.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if(time<100)
						time = time + 1;
					timerBar.setProgress(time);
				}
	        });
	        
	        ImageButton subTime = (ImageButton)dialog.findViewById(R.id.subTime);
	        subTime.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if(time>0)
						time = time - 1;
					timerBar.setProgress(time);
				}
	        });
	        return dialog;

	 }

	 
	 /*
	     * Background thread
	     */
	    private Runnable mUpdateTimeTask=new Runnable() {
			public void run() {
				try{
				//run this thread repeatedly
					MainActivity mainActivity = MainActivity.get(getActivity());
					mainActivity.nPauseSetup();
				}catch(Exception e){
					Log.i("", "mUpdateTimeTask"+e.toString());
				}
			}
		};
	 

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.i("", "onProgressChanged progress :"+progress);
		timer_text.setText(progress+" min");
		time = progress;
		
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}
	 
	 
	 
}
