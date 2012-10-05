package com.blundell.tut.twitterfeedwidget.service;

import com.blundell.tut.twitterfeedwidget.R;
import com.blundell.tut.twitterfeedwidget.persistance.PreferenceConstants;
import com.blundell.tut.twitterfeedwidget.persistance.WidgetPreferences;
import com.blundell.tut.twitterfeedwidget.service.task.RetrieveTweetTask;
import com.blundell.tut.twitterfeedwidget.util.Log;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * This is our service to update the widget, here we will get latest tweet for our search term and display it on the widget
 * Using IntentService allows us to do network calls as it spawns its own threads
 * @author paul.blundell
 *
 */
public class UpdateService extends IntentService {

	public UpdateService() {
		super("UpdateService");
	}

	public UpdateService(String name) {
		super(name);
	}

	public static final String EXTRA_WIDGET_IDS = "com.blundell.tut.provider.UpdateService.EXTRA_WIDGET_IDS";

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("Widget onHandleIntent Update Service started");
		if(intent != null){
            updateAllWidgets(intent);
        }
	}

	private void updateAllWidgets(Intent intent) {
		int[] appWidgetIds = getAppWidgetIdentifiers(intent);
		for (int appWidgetId : appWidgetIds) {
			refreshWidget(appWidgetId);
		}
	}

	private static int[] getAppWidgetIdentifiers(Intent intent) {
		return intent.getIntArrayExtra(EXTRA_WIDGET_IDS);
	}

	private void refreshWidget(int appWidgetId) {
		RemoteViews view = buildViewUpdate(this, appWidgetId);

		updateWidget(appWidgetId, view);
	}

	private static RemoteViews buildViewUpdate(Context context, int appWidgetId) {
		String searchTerm = getTwitterSearchTerm(context);
		String tweet = getLatestTweet(searchTerm);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_twitter);
		views.setTextViewText(R.id.textView_widget_search_term, searchTerm);
		views.setTextViewText(R.id.textView_widget_tweet, tweet);

		return views;
	}

	private static String getTwitterSearchTerm(Context context) {
		WidgetPreferences prefs = getWidgetPreferences(context);
		return prefs.getTwitterSearchTerm();
	}

	private static WidgetPreferences getWidgetPreferences(Context context) {
		return new WidgetPreferences(context.getSharedPreferences(PreferenceConstants.WIDGET_PREFS, MODE_PRIVATE));
	}

	/**
	 * This is the task that will talk to twitter and do the parsing of the response
	 * @param searchTerm
	 * @return the latest tweet for the given search term
	 */
	private static String getLatestTweet(String searchTerm) {
		return new RetrieveTweetTask().retrieveFor(searchTerm);
	}

	private void updateWidget(int appWidgetId, RemoteViews views) {
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(appWidgetId, views);
		Log.d("widget updated:"+ appWidgetId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}