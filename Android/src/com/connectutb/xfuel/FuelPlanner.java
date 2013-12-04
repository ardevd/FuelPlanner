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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FuelPlanner {
	/* Log Tag */
	private final String TAG ="xFuel";
	
	/* Define our variables */
	private final Activity context;
	private final HttpPost httppost;
	private final HttpClient httpclient;
	private boolean bMetar;
	public SharedPreferences settings;
	
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
	private String zero_fuel_weight;
	
	/* Activity Circle */
	private ProgressBar loading;
	
	/* Fuel Data Array */
	private String[] fuelData = new String[0];
	
	public FuelPlanner (Activity context, String aircraft, String orig, String dest, boolean metar, String rules, String units, ProgressBar loading){
		/* Assign variables */
		this.context = context;
		this.httppost = new HttpPost(context.getResources().getString(R.string.post_url));
		this.httpclient = new DefaultHttpClient();
		this.aircraft = aircraft;
		this.orig = orig;
		this.dest = dest;
		this.bMetar = metar;
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
		
		this.loading = loading;
		
		settings =  PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public Document parseFuelResponse(String response){
		/** Need to modify the response string to fix malformed XML **/
		//* Split our string on newlines
		String[] response_array = response.split("(\\n)");
		//Loop through string array
		String fixed_response = "";
			for (int n=0; n<response_array.length-1; n++){
			
				if (n==1){
					//Add a ROOT element <DATA>
					fixed_response += "<DATA>" + System.getProperty("line.separator");
				}
				if(response_array[n].startsWith("<MESSAGES>") || response_array[n].startsWith("</MESSAGES>")){
					//Remove <Messages> tag
				}else{
				fixed_response += response_array[n] + System.getProperty("line.separator");
				}
			}
		//Close the </DATA> tag
		fixed_response += "</DATA>" + System.getProperty("line.separator");
		//Close the XML tag if necessary
		if (fixed_response.contains("</xml>") == false){
		fixed_response += "</XML>" + System.getProperty("line.separator");
		}
		Log.d(TAG, fixed_response);		
		
		//Parse the response string and set values
		//Parse XML Response
		Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(fixed_response));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e(TAG, e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e(TAG, e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
                // return DOM
        	Log.d(TAG, "XML parsing completed successfully");
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
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	public void wantFuelInfo(){
		/** Check if we have network connectivity before proceeding **/
		if (isNetworkAvailable()){
			submitFuelRequest();
		} else{
			// Notify user that an internet connection is required. 
			 Toast.makeText(context, context.getString(R.string.error_network), Toast.LENGTH_SHORT).show(); 
		}
	}
	
	public void submitFuelRequest(){
		
		/** Show loading indicator **/
		loading.setVisibility(View.VISIBLE);
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
					
					Document doc = parseFuelResponse(response);
					NodeList nl = doc.getElementsByTagName("DATA");
					// looping through all item nodes <item>
					for (int i = 0; i < nl.getLength(); i++) {
						/**
						 * HERE WE EXTRACT THE FUEL PLANNING OUTPUT ELEMENTS
						 */
						Element e = (Element) nl.item(i);
					    distance = getValue(e, "NM"); // name child value
					    estimated_fuel_usage = getValue(e, "FUEL_EFU");
					    reserve_fuel = getValue(e, "FUEL_RSV");
					    takeoff_fuel = getValue(e, "FUEL_TOF");
					    estimated_landing_weight = getValue(e, "LWT");
					    estimated_time_enroute = getValue(e, "TIME_BLK");
					    reserve_fuel_time = getValue(e, "TIME_RSV");
					    total_fuel_time = getValue(e, "TIME_TTE");
					    metar_orig = getValue(e, "METAR_ORIG");
					    metar_dest = getValue(e, "METAR_DEST");
					    zero_fuel_weight = getValue(e, "ZFW");
					    
					    String unit_str = "";
					    //Include unit notation if set in settings
					    if (settings.getBoolean("show_units", true)){
					    	
					    	unit_str = " Lbs";
					    if (units.equals("METRIC")){
					    	unit_str = " Kg";		    	
					    }
					    }
					    /** Load up the data in a string array and pass it to the FuelReport activity **/
					    ArrayList<String> fuelData_array = new ArrayList<String>();
					    fuelData_array.add(context.getString(R.string.distance) + "-" + distance + " NM");
					    fuelData_array.add(context.getString(R.string.est_fuel_usage) + "-" + estimated_fuel_usage + unit_str);
					    fuelData_array.add(context.getString(R.string.reserve_fuel) + "-" + reserve_fuel+ unit_str);
					    fuelData_array.add(context.getString(R.string.takeoff_fuel) + "-" + takeoff_fuel+ unit_str);
					    fuelData_array.add(context.getString(R.string.zero_fuel_weight) + "-" + zero_fuel_weight + unit_str);
					    fuelData_array.add(context.getString(R.string.estimated_landing_weight) + "-" + estimated_landing_weight+ unit_str);
					    fuelData_array.add(context.getString(R.string.estimated_time_enroute) + "-" + estimated_time_enroute);
					    fuelData_array.add(context.getString(R.string.reserve_fuel_time) + "-" + reserve_fuel_time);
					    fuelData_array.add(context.getString(R.string.total_fuel_time) + "-" + total_fuel_time);
					    //METAR might not always be included
					    if(bMetar){
					    fuelData_array.add(context.getString(R.string.metar_orig) + "-" + metar_orig);
					    fuelData_array.add(context.getString(R.string.metar_dest) + "-" + metar_dest);
					    }
					    //Convert ArrayList to String Array
					    //Convert the ArrayList to String Array
					    fuelData = (String[]) fuelData_array.toArray(fuelData);
					}
				}catch(ClientProtocolException e){
					//TODO catch block
					Log.d(TAG,"Protocol Error: "+ e.getMessage());
				}catch(IOException e){
					//TODO: catch block
					Log.d(TAG, "IO Error: " + e.getMessage());
				}catch(NullPointerException e){
					Log.d(TAG, "NullPointer Error: " + e.getMessage());
				}
			    //Start FuelReport activity
			    Intent frIntent = new Intent(context, FuelReport.class);
			    frIntent.putExtra("fuelData", fuelData);
			    frIntent.putExtra("aircraft", aircraft);
			    frIntent.putExtra("origin", orig);
			    frIntent.putExtra("destination", dest);
			    frIntent.putExtra("rules",rules);
			    frIntent.putExtra("units", units);
			    frIntent.putExtra("metar", bMetar);
	        	context.startActivity(frIntent);	
	        	
	        	 loading.post(new Runnable() {
	                 public void run() {
	                	 loading.setVisibility(View.GONE);
	                 }
	             });
			}
			
		}).start();
	}
}