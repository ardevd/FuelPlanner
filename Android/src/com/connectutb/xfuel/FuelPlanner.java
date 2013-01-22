package com.connectutb.xfuel;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;

public class FuelPlanner {
	
	/* Define our variables */
	private final Activity context;
	private final HttpPost httppost;
	private final HttpClient httpclient;
	
	public FuelPlanner (Activity context){
		/* Assign variables */
		this.context = context;
		this.httppost = new HttpPost(context.getResources().getString(R.string.post_url));
		this.httpclient = new DefaultHttpClient();
	}

}
