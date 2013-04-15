package com.manoj.macawplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import android.R.color;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manoj.helper.FileHandlers;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;
import com.manoj.listeners.ActivitySwipeDetector;
import com.manoj.listeners.ShakeEventListener;
import com.manoj.listeners.SwipeInterface;

public class MainActivity extends Activity implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener,SwipeInterface {
	
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	private ImageButton btnPlay;
	private ImageButton btnPause;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnShuffle;
	private ImageButton btnPlaylist;
	private ImageButton btnSettings;
	private ImageButton btnRepeat;
	private SeekBar songProgressBar;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private TextView songTitleLable;
	private ImageView coverAlbum;
	private MediaPlayer mp;
	private RelativeLayout homeScreen;

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
	private ArrayList songsList = new ArrayList();
	private SongInfo sInfo=new SongInfo();
	
	private MusicIntentReceiver myReceiver;
	private RemoteControlReceiver remoteControlReceiver;
	private TelephonyManager telephonyManager;
	private FileHandlers fileHandlers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try
		{
			//Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.player);
			
			//swipe listeners
			ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
			RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.homeScreen);
			swipe_layout.setOnTouchListener(swipe);
			
			
			
			
			//shake listener
			mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		    mSensorListener = new ShakeEventListener();   

		    mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

		      public void onShake(float changeSong) {
		        Toast.makeText(MainActivity.this, "Shake!"+changeSong, Toast.LENGTH_SHORT).show();
		        if(changeSong==1){
		        	previousSetup();
		        }else{
		        	nextSetup();
		        }
		      }
		    });
			
			
			
			//headset
			myReceiver = new MusicIntentReceiver();
			IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
			filter.setPriority(1000);
			registerReceiver(myReceiver, filter);
			IntentFilter remoteControlRecieverFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
			remoteControlRecieverFilter.setPriority(1000); 
		    registerReceiver(remoteControlReceiver, remoteControlRecieverFilter);
			fileHandlers = new FileHandlers();
			//1remoteControlReceiver= new RemoteControlReceiver();
			telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			if(telephonyManager != null) {
		    	telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		    }
			
			
	
			// refer all the button variables to actual images
			btnPlay = (ImageButton) findViewById(R.id.btnPlay);
			btnNext = (ImageButton) findViewById(R.id.btnNext);
			btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
			btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
			btnSettings = (ImageButton) findViewById(R.id.btnSettings);
			btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
			btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
			coverAlbum=(ImageView) findViewById(R.id.albumArt);
			songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
			songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
			songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
			songTitleLable = (TextView) findViewById(R.id.songTitle);
			homeScreen = (RelativeLayout) findViewById(R.id.homeScreen);
	
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
			
			//restores previous session
			if (savedInstanceState != null){
			    currentSongIndex = savedInstanceState.getInt("currentSongIndex");
			    playSong(currentSongIndex);
			  }
			
			Resources resources = getResources();
	    	try {
	    	    InputStream rawResource = resources.openRawResource(R.raw.my_config);
	    	    Properties properties = new Properties();
	    	    properties.load(rawResource);
	    	    System.out.println("The properties are now loaded");
	    	    Log.i("main activity","The properties are now loaded");
	    	    System.out.println("properties: " + properties);
	    	    String k=properties.getProperty("#008EBE");
	    	    System.out.println("properties: " + properties);
	    	} catch (IOException e) {
	    	    System.err.println("Failed to open microlog property file");
	    	    e.printStackTrace();
	    	}
			//set the background for app
			//homeScreen.setBackgroundColor(Color.parseColor(fileHandlers.findKeyValue("temp.txt", getApplicationContext(), "backgroundColor")));
			/*String colr=fileHandlers.findKeyValue("temp.txt", getApplicationContext(), "backgroundColor");
			
			ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
			    @Override
			    public Shader resize(int width, int height) {
			    	//RadialGradient lg = new RadialGradient(0, 0,275,Color.parseColor("#0099CC"),Color.parseColor("#0000CC"),
			    	LinearGradient lg = new LinearGradient(0, 0, 0, homeScreen.getHeight(),new int[] {Color.parseColor("#0099CC"),Color.parseColor("#33B5E5"),Color.parseColor("#6DCAEC"),Color.parseColor("#0099CC")},null,
			            Shader.TileMode.REPEAT);
			         return lg;
			    }
			};			
			PaintDrawable p = new PaintDrawable();
			p.setShape(new RectShape());
			p.setShaderFactory(sf);
			homeScreen.setBackgroundDrawable((Drawable)p);*/
			utils.colorSeter(homeScreen, getApplicationContext());
			
			
			
			
			mp.setOnCompletionListener(new OnCompletionListener() {
	            @Override
	            public void onCompletion(MediaPlayer arg0) 
	            {
	            	if(!isRepeat){
	            		if(currentSongIndex<songsList.size()-1){
	            			currentSongIndex++;
	            		}else{
	            			currentSongIndex=0;
	            		}            		
	            	}
	        		playSong(currentSongIndex);	
	            }
	    	});
			
	
			// when play list button is clicked
			btnPlaylist.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							ThemeList.class);
					
					//i.putParcelableArrayListExtra("songList",songsList );
					startActivityForResult(i, 100);
				}
	
			});
			
			// when settings button is clicked
			btnSettings.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							SwipeActivity.class);
					startActivityForResult(i, 101);
				}
	
			});
			
			
			//when repeat is clicked
			btnRepeat.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// repeat the curent song
					if(!isRepeat){
						isRepeat=true;
						btnRepeat.setImageResource(R.drawable.btn_repeatfocussed);
					}else{
						isRepeat=false;
						btnRepeat.setImageResource(R.drawable.btn_repeat);
					}
					
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
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	
	protected void onSaveInstanceState(Bundle bundle) {
		  super.onSaveInstanceState(bundle);
		  bundle.putInt("currentSongIndex", currentSongIndex);
		}
	
	
	
	
	
	public void playSong(int index){
		try{
		mp.reset();
		Song song=(Song)songsList.get(index);
		mp.setDataSource(song.getUrl());
		mp.prepare();
		mp.start();
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
        }else{
        	coverAlbum.setImageResource(R.drawable.images);
        }
        
		songTitleLable.setText(song.getTitle()+" "+currentSongIndex);
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
			songsList = sInfo.getSongs(getContentResolver());
			// play selected song
			playSong(currentSongIndex);
		}

		if(resultCode == 101){
			if(data.getExtras().getInt("temp")==1){
				utils.colorSeter(homeScreen, getApplicationContext());
			}
		}
		
		if (resultCode == 200) {
			Toast.makeText(MainActivity.this, "Shake!"+data.getExtras().getString("album"), Toast.LENGTH_SHORT).show();
			if(data.getExtras().getString("album")!=null){
			songsList = sInfo.getAlbumSongs(getContentResolver(), data.getExtras().getString("album"));
			currentSongIndex=0;
			// play selected song
			playSong(currentSongIndex);
			}
		}
		if (resultCode == 201) {
			Toast.makeText(MainActivity.this, "Shake!"+data.getExtras().getString("artist"), Toast.LENGTH_SHORT).show();
			if(data.getExtras().getString("artist")!=null){
			songsList = sInfo.getArtistSongs(getContentResolver(), data.getExtras().getString("artist"));
			currentSongIndex=0;
			// play selected song
			playSong(currentSongIndex);
			}
		}
		if (resultCode == 202) {
			currentSongIndex = data.getExtras().getInt("songIndex");
			songsList = sInfo.getSongs(getContentResolver());
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
	
	//this class is called when the head set hardware keys are pressed
	public class RemoteControlReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
	            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
	            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
	                // play is pressed
	            	playSetup();
	            }else if (KeyEvent.KEYCODE_MEDIA_PAUSE == event.getKeyCode()) {
	                // pause is pressed
	            	pauseSetup();
	            }else if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == event.getKeyCode()) {
	            	if(playingCurrently){
	    				pauseSetup();
	    				}else{
	    					playSetup();
	    				}
	            }else if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == event.getKeyCode()) {
	                // previous is pressed
	            	previousSetup();
	            }else if (KeyEvent.KEYCODE_MEDIA_NEXT == event.getKeyCode()) {
	                // next is pressed
	            	nextSetup();
	            }
	        }
	    }
	}
	
	//this class is called when the head set is plugged in or out
	private class MusicIntentReceiver extends BroadcastReceiver {
	    @Override public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
	            int state = intent.getIntExtra("state", -1);
	            switch (state) {
	            case 0:
	                //Log.d(TAG, "Headset is unplugged");
	            	mp.pause();
					playingCurrently=false;
					btnPlay.setImageResource(R.drawable.btn_play);
	                break;
	            case 1:
	                //Log.d(TAG, "Headset is plugged");
	            	mp.start();
					btnPlay.setImageResource(R.drawable.btn_pause);
	                break;
	            }
	        }
	    }
	}

	

	//used for track incoming calls
	PhoneStateListener phoneStateListener = new PhoneStateListener() {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	        if (state == TelephonyManager.CALL_STATE_RINGING) {
	            //Incoming call: Pause music
	        	pauseSetup();
	        } else if(state == TelephonyManager.CALL_STATE_IDLE) {
	            //Not in call: Play music
	        	playSetup();
	        } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
	            //A call is dialing, active or on hold
	        }
	        super.onCallStateChanged(state, incomingNumber);
	    }
	};
	
	
	
	
	
//setups
	public void playSetup(){
		mp.start();
	    playingCurrently=true;
		btnPlay.setImageResource(R.drawable.btn_pause);
	}
	public void pauseSetup(){
		mp.pause();
		playingCurrently=false;
		btnPlay.setImageResource(R.drawable.btn_play);
	}
	public void previousSetup(){
		if(currentSongIndex>0){
			playSong(currentSongIndex - 1);
            currentSongIndex = currentSongIndex - 1;
		}else{
			 // play first song
            playSong(songsList.size()-1);
            currentSongIndex = songsList.size()-1;
		}
	}
	public void nextSetup(){
		if(currentSongIndex<(songsList.size()-1)){
			playSong(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
		}else{
			 // play first song
            playSong(0);
            currentSongIndex = 0;
		}
	}
	@Override public void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener,
	            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	            SensorManager.SENSOR_DELAY_UI);
	}
	@Override public void onPause() {
	    //unregisterReceiver(myReceiver);
	    mSensorManager.unregisterListener(mSensorListener);
	    super.onPause();
	}
	
	@Override
	public boolean onKeyDown (int keyCode, KeyEvent event){
		//this method is used for handling the Headeset events
			if( keyCode== KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE||keyCode== KeyEvent.KEYCODE_MEDIA_PLAY||keyCode== KeyEvent.KEYCODE_MEDIA_PAUSE||keyCode== KeyEvent.KEYCODE_MEDIA_STOP){
			if(playingCurrently){
				pauseSetup();
				return true;
				}else{
					playSetup();
					return true;
				}
			}
		return false;
	}


	@Override
	public void bottom2top(View v) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void left2right(View v) {
		// TODO Auto-generated method stub
		Log.i("","LeftToRightSwipe!");
		Intent i = new Intent(getApplicationContext(),
				PlayListActivity.class);
		
		//i.putParcelableArrayListExtra("songList",songsList );
		startActivityForResult(i, 100);
	}


	@Override
	public void right2left(View v) {
		// TODO Auto-generated method stub
		Log.i("","right2left!");
		Intent i = new Intent(getApplicationContext(),
				ThemeList.class);
		startActivityForResult(i, 101);
	}


	@Override
	public void top2bottom(View v) {
		// TODO Auto-generated method stub
		
	}

}