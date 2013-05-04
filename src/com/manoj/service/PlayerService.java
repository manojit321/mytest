package com.manoj.service;


import java.util.Random;

import com.manoj.macawplayer.HomeScreenWidget;
import com.manoj.macawplayer.R;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class PlayerService extends Service {
  private static final String LOG = "de.vogella.android.widget.example";

  @Override
  public void onStart(Intent intent, int startId) {
    Log.i(LOG, "Called");
    // Create some random data

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
        .getApplicationContext());

    int[] allWidgetIds = intent
        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    ComponentName thisWidget = new ComponentName(getApplicationContext(),
        HomeScreenWidget.class);
    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
    Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
    Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));

    for (int widgetId : allWidgetIds) {
      // Create some random data
      int number = (new Random().nextInt(100));

      RemoteViews remoteViews = new RemoteViews(this
          .getApplicationContext().getPackageName(),
          R.layout.homepage_widget_layout);
      Log.w("WidgetExample", String.valueOf(number));
      remoteViews.setImageViewResource(R.id.hw_btnShuffle,R.drawable.bn_shuffle);
      remoteViews.setImageViewResource(R.id.hw_btnPrevious,R.drawable.btn_previous);
      remoteViews.setImageViewResource(R.id.hw_btnPlay,R.drawable.btn_play);
      remoteViews.setImageViewResource(R.id.hw_btnNext,R.drawable.btn_next);
     

      // Register an onClickListener
      Intent clickIntent = new Intent(this.getApplicationContext(),
          HomeScreenWidget.class);

      clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
          allWidgetIds);

      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
          PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.hw_btnNext, pendingIntent);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
    stopSelf();

    super.onStart(intent, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}