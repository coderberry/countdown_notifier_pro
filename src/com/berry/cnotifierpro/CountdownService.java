package com.berry.cnotifierpro;

import java.util.Date;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.RemoteViews;

public class CountdownService extends Service {

	// command strings to send to service
	public static final String UPDATE = "update";
    public static final String PLUS = "plus";
    public static final String MINUS = "minus";
    public static final long MODIFY= 86400;

	@Override
	public void onStart(Intent intent, int startId) {
		//a command, to define what to do, will be important only in the next tutorial part, now there is only update command
		String command = intent.getAction();
		int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
		RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.countdown_widget);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

		SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", 0);
		
//	    //plus button pressed
//	    if (command.equals(PLUS)) {
//	            SharedPreferences.Editor edit=prefs.edit();
//	            edit.putLong("goal" + appWidgetId,prefs.getLong("goal" + appWidgetId, 0)+MODIFY);
//	            edit.commit();
//	            
//	    //minus button pressed
//	    } else if (command.equals(MINUS)) {
//	            SharedPreferences.Editor edit=prefs.edit();
//	            edit.putLong("goal" + appWidgetId,prefs.getLong("goal" + appWidgetId, 0)-MODIFY);
//	            edit.commit();
//	    }
		
		long goal = prefs.getLong("goal" + appWidgetId, 0);
		String title = prefs.getString("title" + appWidgetId, "Countdown Notifier");
		//compute the time left
		long left = goal - new Date().getTime();
		int days = (int) Math.floor(left / (long) (60 * 60 * 24 * 1000));
		left = left - days * (long) (60 * 60 * 24 * 1000);
		int hours = (int) Math.floor(left / (60 * 60 * 1000));
		left = left - hours * (long) (60 * 60 * 1000);
		int mins = (int) Math.floor(left / (60 * 1000));
		left = left - mins * (long) (60 * 1000);
		int secs = (int) Math.floor(left / (1000));
		//put the text into the textView
//		remoteView.setTextViewText(R.id.TextView01, days + " days\n" + hours + " hours " + mins + " mins " + secs + " secs left");
		
//		remoteView.setInt(R.id.widget_container, "setBackgroundResource", R.drawable.widget_background_2);
		
		remoteView.setTextViewText(R.id.title, title);
		
		remoteView.setImageViewResource(R.id.n_day_1, getDrawableIdForNumber(days, 2));
		remoteView.setImageViewResource(R.id.n_day_2, getDrawableIdForNumber(days, 1));
		remoteView.setImageViewResource(R.id.n_day_3, getDrawableIdForNumber(days, 0));
		
		remoteView.setImageViewResource(R.id.n_hour_1, getDrawableIdForNumber(hours, 1));
		remoteView.setImageViewResource(R.id.n_hour_2, getDrawableIdForNumber(hours, 0));
		
		remoteView.setImageViewResource(R.id.n_minute_1, getDrawableIdForNumber(mins, 1));
		remoteView.setImageViewResource(R.id.n_minute_2, getDrawableIdForNumber(mins, 0));
		
		remoteView.setImageViewResource(R.id.n_second_1, getDrawableIdForNumber(secs, 1));
		remoteView.setImageViewResource(R.id.n_second_2, getDrawableIdForNumber(secs, 0));
		
//	    remoteView.setOnClickPendingIntent(R.id.plusbutton, CountdownWidget.makeControlPendingIntent(getApplicationContext(), PLUS,appWidgetId));
//	    remoteView.setOnClickPendingIntent(R.id.minusbutton, CountdownWidget.makeControlPendingIntent(getApplicationContext(), MINUS,appWidgetId));

		// apply changes to widget
		appWidgetManager.updateAppWidget(appWidgetId, remoteView);
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int getDrawableForNumber(int number) {
		switch (number) {
		case 0:
			return R.drawable.n0;
		case 1:
			return R.drawable.n1;
		case 2:
			return R.drawable.n2;
		case 3:
			return R.drawable.n3;
		case 4:
			return R.drawable.n4;
		case 5:
			return R.drawable.n5;
		case 6:
			return R.drawable.n6;
		case 7:
			return R.drawable.n7;
		case 8:
			return R.drawable.n8;
		case 9:
			return R.drawable.n9;
		default:
			return R.drawable.n0;
		}
	}
	
	private int getDrawableIdForNumber(Integer number, Integer idx) {
		if (number <= 9) {
			if (idx == 0) {
				return getDrawableForNumber(number);
			} else {
				return getDrawableForNumber(0);
			}
		} else if (number >= 10 && number <= 99) {
			if (idx == 0) {
				return getDrawableForNumber(Integer.parseInt("" + (number.toString().charAt(1))));
			} else if (idx == 1) {
				return getDrawableForNumber(Integer.parseInt("" + (number.toString().charAt(0))));
			} else {
				return getDrawableForNumber(0);
			}
		} else {
			if (idx == 0) {
				return getDrawableForNumber(Integer.parseInt("" + (number.toString().charAt(2))));
			} else if (idx == 1) {
				return getDrawableForNumber(Integer.parseInt("" + (number.toString().charAt(1))));
			} else {
				return getDrawableForNumber(Integer.parseInt("" + (number.toString().charAt(0))));
			}
		}
	}

}
