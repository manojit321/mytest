package com.example.android.pingme;

import com.manoj.macawplayer.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class TestService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Intent launch = new Intent(this, MainActivity.class);
		launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		launch.setAction(Intent.ACTION_MAIN);
		startActivity(launch);
        return START_NOT_STICKY;
    }
	@Override
	public void onCreate()
	{
		HandlerThread thread = new HandlerThread("TestService", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
	}

}
