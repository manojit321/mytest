
package com.manoj.macawplayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.android.pingme.CommonConstants;
import com.example.android.pingme.PingService;
import com.manoj.helper.FileHandlers;
import com.manoj.helper.PlaylistHandler;
import com.manoj.helper.Song;
import com.manoj.helper.SongInfo;
import com.manoj.helper.Utilities;
import com.manoj.listeners.ShakeEventListener;

public class MainActivity extends Activity implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener/*,SwipeInterface */, Callback{
	
	
	
	
	
	
	
	
	
	public static MainActivity sInstance;
	/**
	 * Object used for PlaybackService startup waiting.
	 */
	private static final Object[] sWait = new Object[0];
	
	
	
	
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	private ImageButton btnPlay;
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
	private ImageView coverAlbumPlay;
	public MediaPlayer mp;
	private RelativeLayout homeScreen;

	// handler to update Ui timer and progressbar
	private Handler myhandle = new Handler();
	private SongManager songManager;
	private Utilities utils;
	public int currentSongIndex = 0;
	public boolean isShuffle = false;
	public boolean isRepeat = false;
	public boolean playingCurrently= false;
	public ArrayList songsList = new ArrayList();
	private SongInfo sInfo=new SongInfo();
	private MusicIntentReceiver myReceiver;
	//private RemoteControlReceiver remoteControlReceiver;
	PlaylistHandler playlistHandler;
	PingService pingService;	
	
	
	
	private Intent mServiceIntent;
	private boolean nChnageSong = false; 
	/**
	 * A Handler running on a worker thread.
	 */
	protected Handler mHandler;
	/**
	 * The looper for the worker thread.
	 */
	protected Looper mLooper;	
	public Handler refresh = new Handler(Looper.getMainLooper());
	
	
	
	private AudioManager mAudioManager;
    private ComponentName mRemoteControlResponder;

	
	
	
	
	
	
	private TelephonyManager telephonyManager;
	private FileHandlers fileHandlers;
	
	//animation for album art
	private GestureDetector gestureDetector;
	
	View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	 private Animation slideRightOut;
	private ViewFlipper viewFlipper;
	private static final int SWIPE_MIN_DISTANCE = 3;
    private static final int SWIPE_MAX_OFF_PATH = 300;
	private static final int SWIPE_THRESHOLD_VELOCITY = 0;
	private boolean playingBeforeCall = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try
		{
			
			
			PingService.addActivity(this);
			sInstance = this;
			 // Creates an explicit Intent to start the service that constructs and
	        // issues the notification.
	        mServiceIntent = new Intent(getApplicationContext(), PingService.class);
			HandlerThread thread = new HandlerThread(getClass().getName(), Process.THREAD_PRIORITY_LOWEST);
			thread.start();

			mLooper = thread.getLooper();
			mHandler = new Handler(mLooper, this);
			
			
	        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, "test");
	        mServiceIntent.setAction(CommonConstants.ACTION_PING);

	        // Launches IntentService "PingService" to set timer.
	        Log.i("","Launches IntentService pingservice from oncreate of mainactivity");
	        stopService(mServiceIntent);
	        startService(mServiceIntent);

			
			
			
			//Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.player);
			
			//swipe listeners
			//ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
			//RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.homeScreen);
			//swipe_layout.setOnTouchListener(swipe);
			
			playlistHandler = new PlaylistHandler(); 
			
			//shake listener
			mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		    mSensorListener = new ShakeEventListener();   

		    mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

		      public void onShake(float changeSong) {
		    	if(playingCurrently)  {
			        Toast.makeText(MainActivity.this, "Shake!"+changeSong, Toast.LENGTH_SHORT).show();
			        Log.i("currentSongIndex ", currentSongIndex+"");
			        if(changeSong==1){
			        	previousSetup();
			        }else{
			        	nextSetup();
			        }
		      	}
		      }
		    });
			
			
			
			//headset
			myReceiver = new MusicIntentReceiver();
			IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
			filter.setPriority(1000);
			registerReceiver(myReceiver, filter);
			fileHandlers = new FileHandlers();
			//remoteControlReceiver= new RemoteControlReceiver();
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
			//coverAlbum=(ImageView) findViewById(R.id.);
			//coverAlbumPreviours=(ImageView) findViewById(R.id.songThumbnailPrevious);
			coverAlbumPlay=(ImageView) findViewById(R.id.songThumbnailPlay);
			//coverAlbumNext=(ImageView) findViewById(R.id.songThumbnailNext);
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
			    int seekTo = savedInstanceState.getInt("seekTo");
			    isRepeat = savedInstanceState.getBoolean("isRepeat");
			    if(isRepeat){
			    	btnRepeat.setImageResource(R.drawable.btn_repeatfocussed);
			    }
			    isShuffle = savedInstanceState.getBoolean("isShuffle");
			    if(isShuffle){
			    	Collections.shuffle(songsList, new Random());
			    	btnShuffle.setImageResource(R.drawable.btn_shufflefocused);
			    	
			    }
			    playSong(currentSongIndex);
			    Log.i("","shuffle"+isShuffle+" currentSon"+currentSongIndex);
			    Log.i("", "from restores previous session"+((Song)songsList.get(currentSongIndex)).getUrl());
			    mp.seekTo(seekTo);
			  }else{
				  playSong(currentSongIndex);
			  }
			
			
			
			
			
			//retrive the previous state of the player
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			currentSongIndex = sharedPref.getInt("currentSongIndex",0);
		    int seekTo = sharedPref.getInt("seekTo",0);
		    isRepeat = sharedPref.getBoolean("isRepeat",false);
		    if(isRepeat){
		    	btnRepeat.setImageResource(R.drawable.btn_repeatfocussed);
		    }
		    isShuffle = sharedPref.getBoolean("isShuffle",false);
		    if(isShuffle){
		    	Collections.shuffle(songsList, new Random());
		    	btnShuffle.setImageResource(R.drawable.btn_shufflefocused);
		    	
		    }
		    playSong(currentSongIndex);
		    Log.i("","shuffle"+isShuffle+" currentSon"+currentSongIndex);
		    Log.i("", "from restores previous session"+((Song)songsList.get(currentSongIndex)).getUrl());
		    if(seekTo!=0)
		    mp.seekTo(seekTo);
		    
		    
		    
		    
		    
		    
		    
		    

			
	/*	for loading form prperty files use the following logic
	 * 	
	 * Resources resources = getResources();
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
	    	}*/
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
			
			//album art animation
			viewFlipper = (ViewFlipper)findViewById(R.id.songThumbnail);
	        slideLeftIn = AnimationUtils.loadAnimation(this, R.drawable.slide_left_in);
	        slideLeftOut = AnimationUtils.loadAnimation(this, R.drawable.slide_left_out);
	        slideRightIn = AnimationUtils.loadAnimation(this, R.drawable.slide_right_in);
	        slideRightOut = AnimationUtils.loadAnimation(this, R.drawable.slide_right_out);
	        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.songThumbnailPlay)));
	        gestureDetector = new GestureDetector(new MyGestureDetector());
	        gestureListener = new View.OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	                if (gestureDetector.onTouchEvent(event)) {
	                    return true;
	                }
	                return false;
	            }
	        };
	        
	        
	        
			
			mp.setOnCompletionListener(new OnCompletionListener() {
	            @Override
	            public void onCompletion(MediaPlayer arg0) 
	            {
	            	Log.i("","onCompletion");
	            	if(!isRepeat){
	            		if(currentSongIndex<songsList.size()-1){
	            			currentSongIndex++;
	            		}else{
	            			currentSongIndex=0;
	            		}            		
	            	}
	            	if(playingCurrently)
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
							SwipeViewActivity.class);
					i.putExtra("songPlaying", currentSongIndex);
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
					updateInfo();
					}else{
						playingCurrently=true;
						mp.start();
						btnPlay.setImageResource(R.drawable.btn_pause);
						updateInfo();
					}
				}
			});
			
			
			//next button
			btnNext.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					nextSetup();
				}
			});
			
			//previous button
					btnPrevious.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v){
							previousSetup();
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
	                    Collections.shuffle(songsList, new Random());
	                    btnShuffle.setImageResource(R.drawable.btn_shufflefocused);
					}
				}
			});
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	
	/*protected void onSaveInstanceState(Bundle bundle) {
		  super.onSaveInstanceState(bundle);
		  Log.i("mainactivity","onSaveInstanceState"+mp.getCurrentPosition());
		  bundle.putInt("currentSongIndex", currentSongIndex);
		  bundle.putInt("seekTo", mp.getCurrentPosition());
		  bundle.putBoolean("isRepeat",isRepeat);
		  bundle.putBoolean("isShuffle",isShuffle);
		  Log.i("", "from restores previous session"+((Song)songsList.get(currentSongIndex)).getUrl());
		  Log.i("","shuffle"+isShuffle+" currentSon"+currentSongIndex);
		}*/
	
	
	
	
	
	public void playSong(int index){
		try{
		Log.i("play", "playSong");
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
            coverAlbumPlay.setImageResource(R.drawable.images);
        } catch (IOException e) {

            e.printStackTrace();
        }
        if(song.getBitmap()!=null){
        	coverAlbumPlay.setImageBitmap(song.getBitmap());
        	coverAlbumPlay.setMinimumHeight(song.getBitmap().getHeight());
        	coverAlbumPlay.setMinimumWidth(song.getBitmap().getWidth());
        }else{
        	coverAlbumPlay.setImageResource(R.drawable.images);
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
        Log.i("notify", "onChangeNotify is called");
        onChangeNotify();
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
			/*if(songsList.isEmpty() && data.getExtras().getString("album").toString() != null){
				songsList = sInfo.getAlbumSongs(getContentResolver(), data.getExtras().getString("album"));
			}*/
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
		if (resultCode == 203) {
			String  title_key= data.getExtras().getString("title_key");
			songsList = sInfo.getSongs(getContentResolver());
			currentSongIndex = sInfo.getSongBasedOnTitle_key(songsList,title_key);
			// play selected song
			playSong(currentSongIndex);
		}
		//get songs of the album and play the given song index
		if(resultCode == 204){
			String  title_key= data.getExtras().getString("title_key");
			String playlist_id = data.getExtras().getString("playlist_id");
			songsList =playlistHandler.songsforplaylists(getApplicationContext(), playlist_id);
			currentSongIndex = sInfo.getSongBasedOnTitle_key(songsList,title_key);
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
			try{
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
			}catch(Exception e){
				Log.i("", "mUpdateTimeTask"+e.toString());
			}
		}
	};


	public void updateInfo() {
        refresh.post(notificationTask);
    }
	private Runnable notificationTask=new Runnable() {
	    public void run()
	    {
	    	if(!playingCurrently){
				btnPlay.setImageResource(R.drawable.btn_play);
			}else{
				btnPlay.setImageResource(R.drawable.btn_pause);
			}
	    	if(nChnageSong){
	    		try{
	    		Log.i("play", "playSong");
	    		mp.reset();
	    		Song song=(Song)songsList.get(currentSongIndex);
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
	                coverAlbumPlay.setImageResource(R.drawable.images);
	            } catch (IOException e) {

	                e.printStackTrace();
	            }
	            if(song.getBitmap()!=null){
	            	coverAlbumPlay.setImageBitmap(song.getBitmap());
	            	coverAlbumPlay.setMinimumHeight(song.getBitmap().getHeight());
	            	coverAlbumPlay.setMinimumWidth(song.getBitmap().getWidth());
	            }else{
	            	coverAlbumPlay.setImageResource(R.drawable.images);
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
	            Log.i("notify", "onChangeNotify is called");
	    	   }catch(Exception e){
	    		   e.printStackTrace();
	    		   nChnageSong = false;
	    	   }
	    		nChnageSong = false;
	    	}

	    	onChangeNotify();
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
	/*public class RemoteControlReceiver extends BroadcastReceiver {
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
	}*/
	
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
	        if (state == TelephonyManager.CALL_STATE_RINGING && playingCurrently) {
	            //Incoming call: Pause music
	        	playingBeforeCall = true;
	        	pauseSetup();
	        } else if(state == TelephonyManager.CALL_STATE_IDLE && playingBeforeCall) {
	            //Not in call: Play music
	        	playSetup();
	        	playingBeforeCall = false;
	        } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
	            //A call is dialing, active or on hold
	        }
	        super.onCallStateChanged(state, incomingNumber);
	    }
	};
	
	
	
	
	
//setups
	public void playSetup(){
		Log.i("Setup", "playSetup");
		mp.start();
	    playingCurrently=true;
	    playingBeforeCall = true;
		btnPlay.setImageResource(R.drawable.btn_pause);
	}
	public void pauseSetup(){
		Log.i("Setup", "pauseSetup");
		mp.pause();
		playingCurrently = false;
		btnPlay.setImageResource(R.drawable.btn_play);
	}
	public void previousSetup(){
		Log.i("Setup", "previousSetup");
		if(currentSongIndex>0){
			currentSongIndex = currentSongIndex - 1;
			playSong(currentSongIndex);
		}else{
			currentSongIndex = songsList.size()-1;
			 // play first song
            playSong(currentSongIndex);
		}
	}
	public void nextSetup(){
		Log.i("Setup", "nextSetup");
		Log.i("Setup", ""+mp.getCurrentPosition());
		if(currentSongIndex<(songsList.size()-1)){
			currentSongIndex = currentSongIndex + 1;
			Log.i("Setup", "above playsong call in nextsetup");
			playSong(currentSongIndex);
		}else{
			currentSongIndex = 0;
			 // play first song
            playSong(currentSongIndex);
		}
	}
	
	//setups for the notification
	public void nPreviousSetup(){
		Log.i("Setup", "previousSetup");
		if(currentSongIndex>0){
			currentSongIndex = currentSongIndex - 1;
		}else{
			currentSongIndex = songsList.size()-1;
		}
		nChnageSong = true;
		updateInfo();
	}
	public void nNextSetup(){
		Log.i("Setup", "nextSetup");
		Log.i("Setup", ""+mp.getCurrentPosition());
		if(currentSongIndex<(songsList.size()-1)){
			currentSongIndex = currentSongIndex + 1;
		}else{
			currentSongIndex = 0;
		}
		nChnageSong = true;
		updateInfo();
	}
	public void nPlaySetup(){
		Log.i("Setup", "playSetup");
		mp.start();
	    playingCurrently=true;
		updateInfo();
	}
	public void nPauseSetup(){
		Log.i("Setup", "pauseSetup");
		mp.pause();
		playingCurrently = false;
		updateInfo();
	}
	
	
	
	
	
	
	
	
	
	@Override public void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener,
	            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	            SensorManager.SENSOR_DELAY_UI);
	}
	@Override public void onPause() {
	    //unregisterReceiver(myReceiver);
	  /*  mSensorManager.unregisterListener(mSensorListener);*/
	    super.onPause();
	}
	
	

	/*@Override
	public void bottom2top(View v) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void left2right(View v) {
		// TODO Auto-generated method stub
		Log.i("","LeftToRightSwipe!");
		Intent i = new Intent(getApplicationContext(),
				CustomizedListView.class);
		
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
		
	}*/

	
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
/*            	Bitmap bitmap = null;
            	Song song = null;
            	for(int i=-1;i<=2;i=i+2){
	            	try {
	            		if(i==-1){
	            			coverAlbum = coverAlbumPreviours;
	            		}else{
	            			coverAlbum = coverAlbumNext;
	            		}
	            		song = (Song)songsList.get(currentSongIndex+i);
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
            	}
*/
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                    nextSetup();
                	viewFlipper.showNext();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation(slideRightIn);
                	viewFlipper.setOutAnimation(slideRightOut);
                	previousSetup();
                	viewFlipper.showPrevious();
                }
            } catch (Exception e) {
                // nothing
            	Log.i("", e.toString());
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
	        return true;
	    else
	    	return false;
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	saveOnStop();
    	Log.i("onstop","onstop");
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	Log.i("ondestroy","ondestroy");
    	mp.release();
    	stopService(mServiceIntent);
    	unregisterReceiver(myReceiver);
  	    mSensorManager.unregisterListener(mSensorListener);
  	    if(pingService!=null)
  	    	pingService.stopNotificattion();
    	PingService.removeActivity(this);
    }
    
    
    
    
    
    
    
    /**
	 * Return the PlaybackService instance, creating one if needed.
	 */
	public static MainActivity get(Context context)
	{
		if (sInstance == null) {
			context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

			while (sInstance == null) {
				try {
					synchronized (sWait) {
						sWait.wait();
					}
				} catch (InterruptedException ignored) {
				}
			}
		}

		return sInstance;
	}
	 public void onChangeNotify() {
	        int seconds;

	        // Gets the reminder text the user entered.
	        String message = "notification";

	        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
	        mServiceIntent.setAction(CommonConstants.ACTION_PING);

	        // Launches IntentService "PingService" to set timer.
	        //startService(mServiceIntent);
	        //stopService(mServiceIntent);
	        

	         pingService = PingService.get(getApplicationContext());
	        if(pingService==null){
	        	Log.i("onChangeNotify","pingService is null");
	        	startService(mServiceIntent);
	        	pingService = PingService.get(getApplicationContext());
	        	pingService.issueNotification(mServiceIntent, message);
	        }
	        else{
	        	Log.i("onChangeNotify","pingService is not null");
	        	pingService.issueNotification(mServiceIntent, message);
	        }
	       }


	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public void saveOnStop(){
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("currentSongIndex", currentSongIndex);
		editor.putInt("seekTo", mp.getCurrentPosition());
		editor.putBoolean("isRepeat",isRepeat);
		editor.putBoolean("isShuffle",isShuffle);
		editor.commit();
	}
	
	public void startTimerToPause(int time){
	}
	
	
}