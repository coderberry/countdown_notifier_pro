package com.berry.cnotifierpro;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class CountdownConfiguration extends Activity {

	private Context self = this;
	private int appWidgetId;
	final String [] arrThemes = new String[]{ "Froyo", "Dark Shadow", "Bold Mist" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get the appWidgetId of the appWidget being configured
		Intent launchIntent = getIntent();
		Bundle extras = launchIntent.getExtras();
		appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

		// set the result for cancel first
		// if the user cancels, then the appWidget
		// should not appear
		Intent cancelResultValue = new Intent();
		cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				appWidgetId);
		setResult(RESULT_CANCELED, cancelResultValue);
		// show the user interface of configuration
		setContentView(R.layout.configuration);

		// Setup Spinner
//		ArrayAdapter<Object> ad = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, arrThemes);
//		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		Spinner spin = (Spinner)findViewById(R.id.themes);
//		spin.setAdapter(ad);

		// the OK button
		Button ok = (Button) findViewById(R.id.okbutton);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// get the date from DatePicker
				DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);
				GregorianCalendar date = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
				
				// Get the title from the edittext view
				EditText fldTitle = (EditText) findViewById(R.id.fld_title);
				String titleName = fldTitle.getText().toString();
				if (titleName.length() == 0) {
					titleName = "Countdown Notifier";
				}
				
//				Spinner txtTheme = (Spinner)findViewById(R.id.themes);
//				String theme = txtTheme.getSelectedItem().toString();

				// save the goal date in SharedPreferences
				// we can only store simple types only like long
				// if multiple widget instances are placed
				// each can have own goal date
				// so store it under a name that contains appWidgetId
				SharedPreferences prefs = self.getSharedPreferences("prefs", 0);
				SharedPreferences.Editor edit = prefs.edit();
				edit.putLong("goal" + appWidgetId, date.getTimeInMillis());
				edit.putString("title" + appWidgetId, titleName);
//				edit.putString("theme" + appWidgetId, theme);
				edit.commit();

				// change the result to OK
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
				setResult(RESULT_OK, resultValue);

				// refresh the appWidget when configuration is done
				PendingIntent updatepending = CountdownWidget.makeControlPendingIntent(self, CountdownService.UPDATE, appWidgetId);
				try {
					updatepending.send();
				} catch (CanceledException e) {
					e.printStackTrace();
				}

				// finish closes activity
				// and sends the OK result
				// the widget will be be placed on the home screen
				finish();
			}
		});

		// cancel button
		Button cancel = (Button) findViewById(R.id.cancelbutton);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// finish sends the already configured cancel result
				// and closes activity
				finish();
			}
		});
	}

	


}
