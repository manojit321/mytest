package com.manoj.macawplayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;

import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener {

	private ImageButton btnPlay;
	private ImageButton btnPause;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnShuffle;
	private ImageButton btnPlaylist;
	private SeekBar songProgressBar;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private TextView songTitleLable;
	private ImageView coverAlbum;
	private MediaPlayer mp;

	// handler to update Ui timer and progressbar
	private Handler myhandle = new Handler();
	private SongManager songManager;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private boolean playingCurrently= false;
	//!private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private ArrayList songsList = new ArrayList();
	private SongInfo sInfo=new SongInfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		// refer all the button variables to actual images
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		coverAlbum=(ImageView) findViewById(R.id.albumArt);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLable = (TextView) findViewById(R.id.songTitle);

		// Media player
		mp = new MediaPlayer();
		songManager = new SongManager();
		utils = new Utilities();

		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this);
		mp.setOnCompletionListener(this);

		// get all songs
		if(songsList.isEmpty()){
		//!songsList = songManager.getPlayList();
			songsList = sInfo.getSongs(getContentResolver());
		}
		
		mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) 
            {
            	if(currentSongIndex<songsList.size()-1){
        			currentSongIndex++;
        		}else{
        			currentSongIndex=0;
        		}
        		playSong(currentSongIndex);	
            }
    	});
		

		// when play list button is clicked
		btnPlaylist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						PlayListActivity.class);
				
				//i.putParcelableArrayListExtra("songList",songsList );
				startActivityForResult(i, 100);
			}

		});
		
		
		
		//play button
		btnPlay.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(playingCurrently){
				mp.pause();
				playingCurrently=false;
				btnPlay.setImageResource(R.drawable.btn_play);
				}else{
					playingCurrently=true;
					mp.start();
					btnPlay.setImageResource(R.drawable.btn_pause);
				}
			}
		});
		
		
		//next button
		btnNext.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(currentSongIndex<(songsList.size()-1)){
					playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
				}else{
					 // play first song
                    playSong(0);
                    currentSongIndex = 0;
				}
			}
		});
		
		//previous button
				btnPrevious.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v){
						if(currentSongIndex>0){
							playSong(currentSongIndex - 1);
		                    currentSongIndex = currentSongIndex - 1;
						}else{
							 // play first song
		                    playSong(songsList.size()-1);
		                    currentSongIndex = songsList.size()-1;
						}
					}
				});
				
		//shuffle_button
		btnShuffle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(isShuffle){
					isShuffle=false;
					songsList = sInfo.getSongs(getContentResolver());
					Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.bn_shuffle);
				}
				else{
					isShuffle=true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    Collections.shuffle(songsList);
                    btnShuffle.setImageResource(R.drawable.btn_shufflefocused);
				}
			}
		});

	
	}
	
	
	public void playSong(int index){
		try{
		mp.reset();
		//!mp.setDataSource(songsList.get(index).get("songPath"));
		Song song=(Song)songsList.get(index);
		mp.setDataSource(song.getUrl());
//		mp.setDataSource(song.getTitle()); 
		mp.prepare();
		mp.start();
		//display title
		//!String title=songsList.get(index).get("songTitle");
		//!songTitleLable.setText(title);
		Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
            		getContentResolver(), song.getAlbumArtUrl());
            if(bitmap!=null)
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            song.setBitmap(bitmap);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            coverAlbum.setImageResource(R.drawable.images);
        } catch (IOException e) {

            e.printStackTrace();
        }
        if(song.getBitmap()!=null){
		coverAlbum.setImageBitmap(song.getBitmap());
		coverAlbum.setMinimumHeight(song.getBitmap().getHeight());
		coverAlbum.setMinimumWidth(song.getBitmap().getWidth());
        }
		songTitleLable.setText(song.getTitle());
		// Changing Button Image to pause image
        btnPlay.setImageResource(R.drawable.btn_pause);
        playingCurrently=true;
        // set Progress bar values
        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);
        
        
        // Updating progress bar
        updateProgressBar();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	}
	
	
		
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			currentSongIndex = data.getExtras().getInt("songIndex");
			// play selected song
			playSong(currentSongIndex);
		}

	}
	
	/**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        myhandle.postDelayed(mUpdateTimeTask, 100);
    }  
 
    /*
     * Background thread
     */
    private Runnable mUpdateTimeTask=new Runnable() {
		public void run() {
			long totalDuration=mp.getDuration();
			long currentDuration=mp.getCurrentPosition();
			//display the time
			songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			
			//update progress bar
			int progress=(int)utils.getProgressPercentage(currentDuration, totalDuration);
			songProgressBar.setProgress(progress);
			
			//run this thread repeatedly
			myhandle.postDelayed(this, 100);
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// when user touch the progress bar remove handler from updating the progress bar
		myhandle.removeCallbacks(mUpdateTimeTask);
		

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// when user stops
		myhandle.removeCallbacks(mUpdateTimeTask);
		int totalDuration=mp.getDuration();
		int currentDuration=utils.progressToTimer(seekBar.getProgress(),totalDuration);
		mp.seekTo(currentDuration);
		updateProgressBar();

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

}
