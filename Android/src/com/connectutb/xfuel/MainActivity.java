package com.connectutb.xfuel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

	/* Our preferences */
	public SharedPreferences settings;
	public SharedPreferences.Editor editor;
	/* Our view components */
	public Spinner aircraftSpinner;
	public EditText orig;
	public EditText dest;
	public Switch metar;
	public RadioButton radioMetrics;
	public RadioButton radioImperial;
	
	public Map<String, String> mMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionBarNavigation();
		
		//Views
		orig = (EditText)findViewById(R.id.editTextOrigin);
		dest = (EditText)findViewById(R.id.editTextDest);
		metar = (Switch) findViewById(R.id.switchWeather);
		
		//Prevent Keyboard from popping up on activity start
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		/** Prepare the Aircraft Array. We are splitting on ; **/
		mMap = new HashMap<String, String>();
		String[] aircraftArray = getResources().getStringArray(R.array.planetypes);
		ArrayList<String> temp_array = new ArrayList<String>();
		for (int i = 0; i < aircraftArray.length; i++){
			//Loop through the aircraftArray and add the stripped strings to the temp_array
			String[] splittedAircraftString = aircraftArray[i].split(";");
			temp_array.add(splittedAircraftString[0]);
			//Add the values to the hash map
			mMap.put(splittedAircraftString[0], splittedAircraftString[1]);
		}
		//Convert the ArrayList to String Array
		aircraftArray = (String[]) temp_array.toArray(aircraftArray);
		
		//Prepare the Aircraft Spinner
		aircraftSpinner = (Spinner)findViewById(R.id.spinnerPlanes);
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   
	    		R.layout.spinner_white, aircraftArray);
	    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    aircraftSpinner.setAdapter(spinnerArrayAdapter);
	    
	    //Init prefs
	    settings = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    //Grab Default values
	    grabDefaults();
	}
	
	public void actionBarNavigation(){
		 //SpinnerAdapter for the ActionBar Navigation
		SpinnerAdapter navSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.app_navigation,
		          R.layout.spinner_white);
		/** Defining Navigation listener */
		/** Defining Navigation listener */
        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
 
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                Toast.makeText(getBaseContext(), "You selected : " + itemPosition  , Toast.LENGTH_SHORT).show();
                return false;
            }
        };
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setListNavigationCallbacks(
	    		navSpinnerAdapter,
	            navigationListener);	
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void grabDefaults(){
		radioMetrics = (RadioButton)findViewById(R.id.radioMetrics);
		radioImperial = (RadioButton)findViewById(R.id.radioImperial);
		Log.d("XFUEL", settings.getString("def_units", "Metrics"));
		if (settings.getString("def_units", "Metrics").equals("Metrics")){
				radioMetrics.setChecked(true);
				
		}else{
			radioImperial.setChecked(true);
		}
		
	}
	
	/* Action on menu selection */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	//Open prefs
    	case R.id.menu_settings:
    		Intent i = new Intent(this, Preferences.class);
        	startActivity(i);	 
    		return true;
    		
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    //Post Fuel request
    public void postFuelRequest(View view){
    	//Do we request weather?
    	boolean getMetar = false;
    	if (metar.isChecked()){
    		getMetar = true;
    	}
    	//Determine units
    	String units = "LBS";
    	if (radioMetrics.isChecked()){
    		units = "METRIC";
    	}
    	
    	FuelPlanner fp = new FuelPlanner(this, mMap.get(aircraftSpinner.getSelectedItem().toString()).toString(), orig.getText().toString(), dest.getText().toString(), getMetar,"JAR", units);
    	fp.submitFuelRequest();
    }
    
    
}
