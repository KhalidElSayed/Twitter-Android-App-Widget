package com.blundell.tut.twitterfeedwidget.service.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import com.blundell.tut.twitterfeedwidget.parser.TwitterJsonSearchParser;
import com.blundell.tut.twitterfeedwidget.util.Log;

/**
 * Connects to the internet using Twitters RESTful API to retrieve a list of tweets
 *
 * Does networking ensure it is not ran on the UI thread
 * @author paul.blundell
 *
 */
public class RetrieveTweetTask {

	// The API to use see: https://dev.twitter.com/docs/using-search
	private static final String TWITTER_SEARCH_API_URL = "http://search.twitter.com/search.json?q=";
	private final HttpClient client;

	public RetrieveTweetTask() {
		client = new DefaultHttpClient();
	}

	/**
	 * Given a search term will return the latest tweet for that term
	 * @param searchTerm the search term to use
	 * @return a tweet or blank if an error occurs
	 */
	public String retrieveFor(String searchTerm) {
		String tweet = "";

		try {
			// Connect to twitter
			HttpGet getTweet = new HttpGet(TWITTER_SEARCH_API_URL+ URLEncoder.encode(searchTerm, "UTF-8"));
			HttpResponse response = client.execute(getTweet);
			InputStream inputStream = response.getEntity().getContent();
			// Parse the returned JSON
			TwitterJsonSearchParser jsonSearchParser = new TwitterJsonSearchParser(inputStream);
			tweet = jsonSearchParser.getLatestTweet();

		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e);
		} catch (UnsupportedEncodingException e){
			Log.e("UnsupportedEncodingException", e);
		} catch (IOException e) {
			Log.e("IOException", e);
		} catch (JSONException e) {
			Log.e("JSONException", e);
		}

		return tweet;
	}
}