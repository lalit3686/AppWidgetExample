package com.example.app.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WordWidget extends AppWidgetProvider {
	
	static final String ACTION_WEBVIEW = "ACTION_WEBVIEW";
	static final String ACTION_REFRESH = "ACTION_REFRESH";
	static int value = 1;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
    	context.startService(new Intent(context, UpdateService.class));
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	super.onReceive(context, intent);
    	if(intent.getAction().equals(ACTION_WEBVIEW)){
    		Log.e("onReceive", "ACTION_WEBVIEW");
    		Intent action = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/"));
    		action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		context.startActivity(action);
    	}
    	else if(intent.getAction().equals(ACTION_REFRESH)){
    		context.startService(new Intent(context, UpdateService.class));
    		Log.e("onReceive", "ACTION_REFRESH");
    	}
    }
    
    public static class UpdateService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
            // Build the widget update for today
            RemoteViews updateViews = buildUpdate(this);
            
            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, WordWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

        public RemoteViews buildUpdate(Context context) {
            
        	RemoteViews updateViews;
            // Build an update that holds the updated widget contents
            updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_word);
            
            updateViews.setTextViewText(R.id.word_title, String.valueOf(value));
            Log.e("value", String.valueOf(value));
            value += 1;
            
            Intent action = new Intent(context, WordWidget.class);
            action.setAction(ACTION_WEBVIEW);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, action, 0);
            updateViews.setOnClickPendingIntent(R.id.icon, pendingIntent);
            
            action = new Intent(context, WordWidget.class);
            action.setAction(ACTION_REFRESH);
            PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(context,0, action, 0);
            updateViews.setOnClickPendingIntent(R.id.button_widget, pendingIntentRefresh);
            
            return updateViews;
        }
          
        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }
}
