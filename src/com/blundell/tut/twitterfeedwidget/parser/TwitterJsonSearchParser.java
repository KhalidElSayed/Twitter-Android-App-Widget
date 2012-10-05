package com.blundell.tut.twitterfeedwidget.parser;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blundell.tut.twitterfeedwidget.util.Log;
import com.blundell.tut.twitterfeedwidget.util.StreamConverter;

/**
 * Parses the JSON returned from the Twitter API
 * See here for the format: https://dev.twitter.com/docs/api/1/get/search
 * @author paul.blundell
 *
 */
public class TwitterJsonSearchParser {

	private final JSONObject jsonObject;

	public TwitterJsonSearchParser(InputStream inputStream) throws JSONException {
		jsonObject = StreamConverter.convertStreamToJsonObject(inputStream);
	}

	public String getLatestTweet() {
		String tweet = "Error";
		try {
			tweet = parseJsonForLatestTweet();
		} catch (JSONException e) {
			Log.e("Failed to get latest tweet", e);
		}
		return tweet;
	}

	private String parseJsonForLatestTweet() throws JSONException {
		JSONArray jsonTweets = getTweetResults();
		JSONObject jsonFirstTweet = getLatestTweet(jsonTweets);

		String fromUser = getUserOfTweet(jsonFirstTweet);
		String tweetText = getTweet(jsonFirstTweet);

		return createTweet(fromUser, tweetText);
	}

	private JSONArray getTweetResults() throws JSONException {
		return jsonObject.getJSONArray("results");
	}

	private static JSONObject getLatestTweet(JSONArray json) throws JSONException {
		return json.getJSONObject(0);
	}

	private static String getUserOfTweet(JSONObject json) throws JSONException {
		return json.getString("from_user");
	}

	private static String getTweet(JSONObject json) throws JSONException {
		return json.getString("text");
	}

	private static String createTweet(String user, String text) {
		return user + ": " + text;
	}
}