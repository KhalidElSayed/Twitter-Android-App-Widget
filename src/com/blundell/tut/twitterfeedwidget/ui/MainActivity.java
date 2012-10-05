package com.blundell.tut.twitterfeedwidget.ui;

import com.blundell.tut.twitterfeedwidget.FromXML;
import com.blundell.tut.twitterfeedwidget.R;
import com.blundell.tut.twitterfeedwidget.persistance.PreferenceConstants;
import com.blundell.tut.twitterfeedwidget.persistance.WidgetPreferences;
import com.blundell.tut.twitterfeedwidget.receiver.TwitterWidgetProvider;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A config style activity, here you can set the search term that the widget will show the first tweet for. This is saved into SharedPreferences
 * and used by the widget to show the tweets.
 * you can also reopen this activity to change the search term.
 * If you check the twitter_appwidget_info.xml file the current update time for this widget is every 30 minutes.
 *
 * @author paul.blundell
 *
 */
public class MainActivity extends Activity {

	// The preferences to remember the search term
    private WidgetPreferences widgetPrefs;
	private TextView currentTwitterSearchTermTextView;
	private EditText newTwitterSearchTermEditText;
	private TextView addWidgetInstructionsTextView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgetPreferences();
        initViews();
        populateViews();
    }

	/**
	 * This is the onClick to change the search term, we don't do any validation on the input
	 * This will save the search term, update the activity UI and also update any currently added widgets
	 * @param button
	 */
	@FromXML
	public void onSetTwitterSearchTermClick(View button){
		String newSearchTerm = getInputTwitterSearchTerm();
		widgetPrefs.saveTwitterSearchTerm(newSearchTerm);
		currentTwitterSearchTermTextView.setText(newSearchTerm);
		addWidgetInstructionsTextView.setVisibility(View.VISIBLE);
		updateAllWidgets();
	}

	private void initWidgetPreferences() {
		SharedPreferences sharedPreferences = getSharedPreferences(PreferenceConstants.WIDGET_PREFS, MODE_PRIVATE);
        widgetPrefs = new WidgetPreferences(sharedPreferences);
	}

	private void initViews() {
		currentTwitterSearchTermTextView = (TextView) findViewById(R.id.textView_twitter_search_term);
		newTwitterSearchTermEditText = (EditText) findViewById(R.id.editText_twitter_search_term);
		addWidgetInstructionsTextView = (TextView) findViewById(R.id.textView_further_instructions);
	}

	private void populateViews() {
		currentTwitterSearchTermTextView.setText(widgetPrefs.getTwitterSearchTerm());
	}

	private String getInputTwitterSearchTerm() {
		return newTwitterSearchTermEditText.getText().toString();
	}

	private void updateAllWidgets(){
	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
	    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TwitterWidgetProvider.class));
	    if (appWidgetIds.length > 0) {
	        new TwitterWidgetProvider().onUpdate(this, appWidgetManager, appWidgetIds);
	    }
	}
}