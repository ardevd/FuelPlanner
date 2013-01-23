package com.connectutb.xfuel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.connectutb.xfuel.util.DbManager;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	/* Our preferences */
	public SharedPreferences settings;
	public SharedPreferences.Editor editor;
	/* Our view components */
	public Spinner aircraftSpinner;
	public Spinner rulesSpinner;
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
	    ArrayAdapter<String> aircraftSpinnerArrayAdapter = new ArrayAdapter<String>(this,   
	    		R.layout.spinner_white, aircraftArray);
	    aircraftSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    aircraftSpinner.setAdapter(aircraftSpinnerArrayAdapter);
	    
	    //Prepare the Rules spinner
		rulesSpinner = (Spinner)findViewById(R.id.spinnerRules);
	    ArrayAdapter rulesSpinnerArrayAdapter = ArrayAdapter.createFromResource( this, R.array.rules , R.layout.spinner_white); 
	    rulesSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    rulesSpinner.setAdapter(rulesSpinnerArrayAdapter);
	    
	    //Init prefs
	    settings = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    //Grab Default values
	    grabDefaults();
	    
		//Set Fonts
		TextView xfuel_title = (TextView)findViewById(R.id.fuelTitle);
		TextView aircraft_title = (TextView)findViewById(R.id.textViewPlanesTitle);
		TextView orig_title = (TextView)findViewById(R.id.TextViewOrigin);
		TextView dest_title = (TextView)findViewById(R.id.TextViewDest);
		TextView rules = (TextView)findViewById(R.id.TextViewRules);
		Typeface tf = Typeface.createFromAsset(getAssets(),
	            "fonts/Roboto-Light.ttf");
		xfuel_title.setTypeface(tf);
		aircraft_title.setTypeface(tf);
		orig_title.setTypeface(tf);
		dest_title.setTypeface(tf);
		metar.setTypeface(tf);
		orig.setTypeface(tf);
		dest.setTypeface(tf);
		radioMetrics.setTypeface(tf);
		radioImperial.setTypeface(tf);
		rules.setTypeface(tf);
		
		//Auto fill default airports if set
		if (settings.getBoolean("use_default_airports", false) == true){
			orig.setText(settings.getString("default_origin", ""));
			dest.setText(settings.getString("default_destination", ""));
		}
				
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
    		Intent settingsIntent = new Intent(this, Preferences.class);
        	startActivity(settingsIntent);	 
    		return true;
    	
    	case R.id.menu_history:
    		Intent historyIntent = new Intent(this, FuelHistory.class);
        	startActivity(historyIntent);	 
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
    	
        //Define progressDialog
        ProgressBar progress = (ProgressBar)findViewById(R.id.progress);
    	FuelPlanner fp = new FuelPlanner(this, mMap.get(aircraftSpinner.getSelectedItem().toString()).toString(), orig.getText().toString(), dest.getText().toString(), getMetar,rulesSpinner.getSelectedItem().toString(), units, progress);
    	//add to history
    	//Define our database manager
        DbManager db = new DbManager(this);
        
        db.addToHistory(mMap.get(aircraftSpinner.getSelectedItem().toString()).toString(), orig.getText().toString(), dest.getText().toString(), getMetar,rulesSpinner.getSelectedItem().toString(), units);
    	fp.submitFuelRequest();
    }
    
    
}
