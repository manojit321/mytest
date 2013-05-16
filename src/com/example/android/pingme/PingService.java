/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.android.pingme;





import java.util.ArrayList;



import com.manoj.helper.Song;
import com.manoj.macawplayer.MainActivity;
import com.manoj.macawplayer.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.RemoteViews;

/**
 * PingService creates a notification that includes 2 buttons: one to snooze the
 * notification, and one to dismiss it.
 */
public class PingService extends IntentService implements OnCompletionListener {

    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;
    NotificationCompat.Builder builder;
    public static final String ACTION_TOGGLE_PLAYBACK_NOTIFICATION = "org.manoj.action.ACTION_TOGGLE_PLAYBACK_NOTIFICATION";
    public static final String ACTION_NEXT = "org.manoj.action.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "org.manoj.action.ACTION_PREVIOUS";
    MainActivity main;
    public static PingService sInstance;
    private static MainActivity sActivities = null;
    private MediaPlayer mp;

    public PingService() {

        // The super call is required. The background thread that IntentService
        // starts is labeled with the string argument you pass.
        super("com.example.android.pingme");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	//mp = new MediaPlayer();
    	//mp.setOnCompletionListener(this);
    	sInstance = this;
    	Log.i("onHandleIntent", "onHandleIntent");
    	/************/
    	main = MainActivity.get(getApplicationContext());
    	//main = sActivities;
    	if(main == null){
    		main = MainActivity.get(getApplicationContext());
    	}
    	
    	
    	
    	
    	
    	
    	// The reminder message the user set.
        mMessage = intent.getStringExtra(CommonConstants.EXTRA_MESSAGE);
        // The timer duration the user set. The default is 10 seconds.
        mMillis = intent.getIntExtra(CommonConstants.EXTRA_TIMER,
                CommonConstants.DEFAULT_TIMER_DURATION);
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        String action = intent.getAction();
        // This section handles the 3 possible actions:
        // ping, snooze, and dismiss.
        if(action.equals(CommonConstants.ACTION_PING)) {
            issueNotification(intent, mMessage);
        } else if (action.equals(CommonConstants.ACTION_SNOOZE)) {
            nm.cancel(CommonConstants.NOTIFICATION_ID);
            Log.d(CommonConstants.DEBUG_TAG, getString(R.string.snoozing));
            // Sets a snooze-specific "done snoozing" message.
            issueNotification(intent, getString(R.string.done_snoozing));

        } else if (action.equals(CommonConstants.ACTION_DISMISS)) {
            nm.cancel(CommonConstants.NOTIFICATION_ID);
        } 
        
        
        
        
        
        
        
        
        
        
        
        
        
        else if (ACTION_TOGGLE_PLAYBACK_NOTIFICATION.equals(action)) {
        	if(main.playingCurrently){
        		main.nPauseSetup(main);
        		//  main.finish();
        		  //nm.cancel(CommonConstants.NOTIFICATION_ID);
        	}else
        		main.nPlaySetup();
        }
        else if (ACTION_NEXT.equals(action)) {
        	Log.i("pingservice", "ACTION_NEXT is called");
        	// Starting new intent
			main.nNextSetup();
        } else if (ACTION_PREVIOUS.equals(action)) {
			main.nPreviousSetup();
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
    }

    public void issueNotification(Intent intent, String msg) {
    	Log.i("issueNotification", "issueNotification");
    	sInstance = this;
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

          
        
        

        // Constructs the Builder object.
        builder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle("notification")
                .setContentText(getString(R.string.ping))
                .setContent(createNotification())
                 // requires VIBRATE permission
                /*
                 * Sets the big view "big text" style and supplies the
                 * text (the user's reminder message) that will be displayed
                 * in the detail area of the expanded notification.
                 * These calls are ignored by the support library for
                 * pre-4.1 devices.
                 */
                .setStyle(new NotificationCompat.BigTextStyle()
                     .bigText(msg));
/*
        
         * Clicking the notification itself displays ResultActivity, which provides
         * UI for snoozing or dismissing the notification.
         * This is available through either the normal view or big view.
         
         Intent resultIntent = main.getIntent();
         resultIntent.putExtra(CommonConstants.EXTRA_MESSAGE, msg);
         resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

         // Because clicking the notification opens a new ("special") activity, there's
         // no need to create an artificial back stack.
         PendingIntent resultPendingIntent =
                 PendingIntent.getActivity(
                 this,
                 0,
                 resultIntent,
                 PendingIntent.FLAG_UPDATE_CURRENT
         );

         builder.setContentIntent(resultPendingIntent);*/
         startTimer(mMillis);
    }

    private void issueNotification(NotificationCompat.Builder builder) {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Including the notification ID allows you to update the notification later on.
        mNotificationManager.notify(CommonConstants.NOTIFICATION_ID, builder.build());
    }

 // Starts the timer according to the number of seconds the user specified.
    private void startTimer(int millis) {
    	millis = 1;
        Log.d(CommonConstants.DEBUG_TAG, getString(R.string.timer_start));
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
            Log.d(CommonConstants.DEBUG_TAG, getString(R.string.sleep_error));
        }
        Log.d(CommonConstants.DEBUG_TAG, getString(R.string.timer_finished));
        issueNotification(builder);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	/**
	 * Create a song notification. Call through the NotificationManager to
	 * display it.
	 *
	 * @param song The Song to display information about.
	 * @param state The state. Determines whether to show paused or playing icon.
	 */
	public RemoteViews createNotification()
	{
		
		/************/
    	main = MainActivity.get(getApplicationContext());
    	//main = sActivities;
		
		//boolean playing = (state & FLAG_PLAYING) != 0;

		RemoteViews views = new RemoteViews(getPackageName(), R.layout.playernotificationlayout);

		/*Bitmap cover = song.getCover(this);
		if (cover == null) {
			views.setImageViewResource(R.id.cover, R.drawable.fallback_cover);
		} else {
			views.setImageViewBitmap(R.id.cover, cover);
		}*/

		Song song = (Song)main.songsList.get(main.currentSongIndex);

		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			int playButton = main.playingCurrently ? R.drawable.btn_pause : R.drawable.btn_play;
			views.setImageViewResource(R.id.n_btnplay, playButton);

			ComponentName service = new ComponentName(this, PingService.class);

			Intent playPause = new Intent(PingService.ACTION_TOGGLE_PLAYBACK_NOTIFICATION);
			playPause.setComponent(service);
			views.setOnClickPendingIntent(R.id.n_btnplay, PendingIntent.getService(this, 0, playPause, 0));

			Intent next = new Intent(PingService.ACTION_NEXT);
			next.setComponent(service);
			views.setOnClickPendingIntent(R.id.n_btnnext, PendingIntent.getService(this, 0, next, 0));

			Intent close = new Intent(PingService.ACTION_PREVIOUS);
			close.setComponent(service);
			views.setOnClickPendingIntent(R.id.n_btnprevious, PendingIntent.getService(this, 0, close, 0));
	/*	} else if (!playing) {
			//title = getResources().getString(R.string.notification_title_paused, song.title);
		}
*/
		views.setTextViewText(R.id.title, song.getTitle());
		views.setTextViewText(R.id.artist, song.getArtist());
		if(song.getBitmap()==null)
			views.setImageViewResource(R.id.n_cover, R.drawable.images);
		else
			views.setImageViewBitmap(R.id.n_cover, song.getBitmap());
		
		
		
		/*if (mInvertNotification) {
			views.setTextColor(R.id.title, 0xffffffff);
			views.setTextColor(R.id.artist, 0xffffffff);
		}*/

		/*Notification notification = new Notification();
		notification.contentView = views;
		notification.icon = R.drawable.status_icon;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.contentIntent = mNotificationAction;*/
		return views;
	}
	/**
	 * Return the PlaybackService instance, creating one if needed.
	 */
	public static PingService get(Context context)
	{
		return sInstance;
	}
	
	//service
	public void playSongService(String url){
		try{
		mp.reset();
		mp.setDataSource(url);
		mp.prepare();
		mp.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public long getDurrationService(){
		return mp.getDuration();
	}
	public long getCurrentPositionService(){
		return mp.getCurrentPosition();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Add an Activity to the registered PlaybackActivities.
	 *
	 * @param activity The Activity to be added
	 */
	public static void addActivity(MainActivity activity)
	{
		sActivities = activity;
	}
	/**
	 * Remove an Activity from the registered PlaybackActivities
	 *
	 * @param activity The Activity to be removed
	 */
	public static void removeActivity(MainActivity activity)
	{
		sActivities = activity;
	}

	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    
    
    
    
    
}
