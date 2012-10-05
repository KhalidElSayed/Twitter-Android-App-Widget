package com.blundell.tut.twitterfeedwidget.persistance;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 * @author paul.blundell
 *
 */
public class WidgetPreferences {

	private final SharedPreferences sharedPreferences;
	private final Editor editor;

	public WidgetPreferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
		editor = sharedPreferences.edit();
	}

	public String getTwitterSearchTerm(){
		return getString(PreferenceConstants.TWITTER_SEARCH_TERM);
	}

	public void saveTwitterSearchTerm(String searchTerm){
		saveString(PreferenceConstants.TWITTER_SEARCH_TERM, searchTerm);
	}

	private String getString(String key) {
		return sharedPreferences.getString(key, "");
	}

	private void saveString(String key, String value){
		editor.putString(key, value).commit();
	}

}
