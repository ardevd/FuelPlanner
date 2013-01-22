package com.connectutb.xfuel;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.util.Log;

public class FuelPlanner {
	
	/* Define our variables */
	private final Activity context;
	private final HttpPost httppost;
	private final HttpClient httpclient;
	
	/* Input Parameters */
	private final String aircraft;
	private final String orig;
	private final String dest;
	private final String metar;
	private final String rules;
	private final String units;
	private final String account;
	private final String license;
	private final String email;
	
	/* Output Parameters */
	private String distance;
	private String estimated_fuel_usage;
	private String reserve_fuel;
	private String takeoff_fuel;
	private String estimated_landing_weight;
	private String estimated_time_enroute;
	private String reserve_fuel_time;
	private String total_fuel_time;
	private String metar_orig;
	private String metar_dest;
	
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
	
	public Document parseFuelResponse(String response){
		
		//Parse the response string and set values
		//Parse XML Response
		Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(response));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e("XFUEL: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("XFUEL: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("XFUEL: ", e.getMessage());
                return null;
            }
                // return DOM
        	Log.d("XFUEL", "XML parsing completed successfully");
            return doc;
           
	}
	
	public String getValue(Element item, String str) {
		 NodeList n = item.getElementsByTagName(str);
		 return this.getElementValue(n.item(0));
		}

	public final String getElementValue( Node elem ) {
	     Node child;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                 if( child.getNodeType() == Node.TEXT_NODE  ){
	                     return child.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	} 
	
	public void submitFuelRequest(){
		
		/** Post fuel data to server **/
		new Thread(new Runnable(){
			public void run(){
				try{
					//add data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("QUERY", "FUEL"));
					nameValuePairs.add(new BasicNameValuePair("EQPT", aircraft));
					nameValuePairs.add(new BasicNameValuePair("ORIG", orig));
					nameValuePairs.add(new BasicNameValuePair("DEST", dest));
					nameValuePairs.add(new BasicNameValuePair("RULES", rules));
					nameValuePairs.add(new BasicNameValuePair("UNITS", units));
					nameValuePairs.add(new BasicNameValuePair("METAR", metar));
					nameValuePairs.add(new BasicNameValuePair("USER", email));
					nameValuePairs.add(new BasicNameValuePair("ACCOUNT", account));
					nameValuePairs.add(new BasicNameValuePair("LICENSE", license));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					//Execute HTTP Post
					String response = httpclient.execute(httppost,responseHandler);
					
					parseFuelResponse(response);
					Log.d("XFUEL", "Server Response: " + response);
				}catch(ClientProtocolException e){
					//TODO catch block
				}catch(IOException e){
					//TODO: catch block
				}
			}
		}).start();
	}
}
