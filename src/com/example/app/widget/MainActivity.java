package com.example.app.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);
		Button button = (Button) findViewById(R.id.button);
		button.setText("Update Widget");
		button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Widget Updated - "+WordWidget.value, Toast.LENGTH_SHORT).show();
				updateWidget();
			}
		});
	}
	
	private void updateWidget() {
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		int[] appId = manager.getAppWidgetIds(new ComponentName(getPackageName(), WordWidget.class.getSimpleName()));
		new WordWidget().onUpdate(this, manager, appId);
	}
}
