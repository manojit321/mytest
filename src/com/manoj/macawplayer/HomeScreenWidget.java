package com.manoj.macawplayer;


import java.util.Random;



import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class HomeScreenWidget extends AppWidgetProvider {

  private static final String ACTION_CLICK = "ACTION_CLICK";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {

    // Get all ids
    ComponentName thisWidget = new ComponentName(context,
    		HomeScreenWidget.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    for (int widgetId : allWidgetIds) {
      // Create some random data
      int number = (new Random().nextInt(100));

      RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
          R.layout.homepage_widget_layout);
      Log.w("WidgetExample", String.valueOf(number));
      // Set the text
      //remoteViews.setTextViewText(R.id.update, String.valueOf(number));
      remoteViews.setImageViewResource(R.id.hw_btnShuffle,R.drawable.bn_shuffle);
      remoteViews.setImageViewResource(R.id.hw_btnPrevious,R.drawable.btn_previous);
      remoteViews.setImageViewResource(R.id.hw_btnPlay,R.drawable.btn_play);
      remoteViews.setImageViewResource(R.id.hw_btnNext,R.drawable.btn_next);
      
      
      Intent intent;
      PendingIntent pendingIntent;

      
      ComponentName service = new ComponentName(context, MainActivity.class);

		intent = new Intent(context, MainActivity.class);
		
		pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btnShuffle, pendingIntent);
		
		intent = new Intent().setComponent(service);
		pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btnPrevious, pendingIntent);

		pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btnPlay, pendingIntent);

		pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btnNext, pendingIntent);


		appWidgetManager.updateAppWidget(new ComponentName(context, HomeScreenWidget.class), remoteViews);
      
      
      

     /* // Register an onClickListener
      Intent intent = new Intent(context, HomeScreenWidget.class);

      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

      PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
          0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      //remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
*/    }
  }
}