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

public class FuelPlanner {
	
	/* Define our variables */
	private final Activity context;
	private final HttpPost httppost;
	private final HttpClient httpclient;
	
	private final String aircraft;
	
	
	public FuelPlanner (Activity context, String aircraft){
		/* Assign variables */
		this.context = context;
		this.httppost = new HttpPost(context.getResources().getString(R.string.post_url));
		this.httpclient = new DefaultHttpClient();
		this.aircraft = aircraft;
	}

	public void submitFuelRequest(){
		
		/** Post fuel data to server **/
		new Thread(new Runnable(){
			public void run(){
				try{
					//add data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("aircraft", aircraft));
					
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
