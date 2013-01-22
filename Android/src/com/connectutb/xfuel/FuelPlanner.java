package com.connectutb.xfuel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class FuelPlanner {
	
	/* Define our variables */
	private final Activity context;
	private final HttpPost httppost;
	private final HttpClient httpclient;
	
	private final String aircraft;
	private final String orig;
	private final String dest;
	private final String metar;
	private final String rules;
	private final String units;
	private final String account;
	private final String license;
	private final String email;
	
	
	public FuelPlanner (Activity context, String aircraft, String orig, String dest, boolean metar, String rules, String units){
		/* Assign variables */
		this.context = context;
		this.httppost = new HttpPost(context.getResources().getString(R.string.post_url));
		this.httpclient = new DefaultHttpClient();
		this.aircraft = aircraft;
		this.orig = orig;
		this.dest = dest;
		if(metar){
			this.metar = "YES";
		}else{
			this.metar = "NO";
		}
		this.rules = rules;
		this.units = units;
		/** API license information */
		this.account = context.getString(R.string.account);
		this.license = context.getString(R.string.license);
		this.email = context.getString(R.string.email);
	    
	}

	public void submitFuelRequest(){
		
		/** Post fuel data to server **/
		new Thread(new Runnable(){
			public void run(){
				try{
					//add data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("EQPT", aircraft));
					nameValuePairs.add(new BasicNameValuePair("ORIG", orig));
					nameValuePairs.add(new BasicNameValuePair("DEST", dest));
					nameValuePairs.add(new BasicNameValuePair("RULES", rules));
					nameValuePairs.add(new BasicNameValuePair("metar", metar));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
					//Execute HTTP Post
					httpclient.execute(httppost);
					
				}catch(ClientProtocolException e){
					//TODO catch block
				}catch(IOException e){
					//TODO: catch block
				}
			}
		}).start();
	}
}
