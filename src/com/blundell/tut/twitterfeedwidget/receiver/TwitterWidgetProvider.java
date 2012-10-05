package com.blundell.tut.twitterfeedwidget.receiver;

import com.blundell.tut.twitterfeedwidget.service.UpdateService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * Starts our service where we will update the widgets
 *
 * @author paul.blundell
 *
 */
public class TwitterWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra(UpdateService.EXTRA_WIDGET_IDS, appWidgetIds);
        context.startService(intent);
	}

}
