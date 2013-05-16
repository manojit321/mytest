package com.manoj.macawplayer;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.example.android.pingme.TestService;

public class TestActivity extends Activity implements Callback{

	/**
	 * A Handler running on the UI thread, in contrast with mHandler which runs
	 * on a worker thread.
	 */
	protected final Handler mUiHandler = new Handler(this);
	/**
	 * A Handler running on a worker thread.
	 */
	protected Handler mHandler;
	/**
	 * The looper for the worker thread.
	 */
	protected Looper mLooper;
	
	@Override
	public void onCreate(Bundle state)
	{
		super.onCreate(state);


		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		HandlerThread thread = new HandlerThread(getClass().getName(), Process.THREAD_PRIORITY_LOWEST);
		thread.start();

		mLooper = thread.getLooper();
		mHandler = new Handler(mLooper, this);
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onStart()
	{
		super.onStart();
			startService(new Intent(this, TestService.class));

	}

}
